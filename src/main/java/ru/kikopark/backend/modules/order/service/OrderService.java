package ru.kikopark.backend.modules.order.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.modules.base.dto.CreateInstitutionTicketToRemote;
import ru.kikopark.backend.modules.base.repositories.InstitutionsRepository;
import ru.kikopark.backend.modules.order.dto.*;
import ru.kikopark.backend.modules.order.entities.*;
import ru.kikopark.backend.modules.order.repositories.*;
import ru.kikopark.backend.utils.*;
//import ru.kikopark.backend.utils.EmailService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    PrintService printService;

    EmailService emailService;

//    MailerSender mailerSender;

    OrdersRepository ordersRepository;
    OrderItemsRepository orderItemsRepository;
    StatusesRepository statusesRepository;
    TypesRepository typesRepository;
    CouponsRepository couponsRepository;
    InstitutionTicketsRepository institutionTicketsRepository;
    InstitutionsRepository institutionsRepository;
    TimesRepository timesRepository;
    TicketsRepository ticketsRepository;

    PaidPersonRepository paidPersonRepository;

    StatusEntity STATUS_PAID;
    StatusEntity STATUS_USING;
    StatusEntity STATUS_USED;

    public OrderService(EmailService emailService, PrintService printService, PaidPersonRepository paidPersonRepository, TicketsRepository ticketsRepository, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, StatusesRepository statusesRepository, TypesRepository typesRepository, CouponsRepository couponsRepository, InstitutionTicketsRepository institutionTicketsRepository, InstitutionsRepository institutionsRepository, TimesRepository timesRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.statusesRepository = statusesRepository;
        this.typesRepository = typesRepository;
        this.couponsRepository = couponsRepository;
        this.institutionTicketsRepository = institutionTicketsRepository;
        this.institutionsRepository = institutionsRepository;
        this.timesRepository = timesRepository;
        this.ticketsRepository = ticketsRepository;
        this.paidPersonRepository = paidPersonRepository;
        this.printService = printService;
//        this.mailerSender = mailerSender;
        this.emailService = emailService;
        this.STATUS_PAID = statusesRepository.getStatusEntityByStatusId(1);
        this.STATUS_USING = statusesRepository.getStatusEntityByStatusId(2);
        this.STATUS_USED = statusesRepository.getStatusEntityByStatusId(3);
    }

    public Object getTicketsByOrder(UUID id) {
        Optional<OrderEntity> returnedOrderEntity = ordersRepository.getOrderEntityByOrderId(id);
        if (returnedOrderEntity.isPresent()) {
            OrderEntity orderEntity = returnedOrderEntity.get();
            if (!orderEntity.getStatus().equals(STATUS_USED)) {
                List<OrderItemEntity> orderItemEntities = orderItemsRepository.getOrderItemEntitiesByOrderId(id);
                return orderItemEntities.stream()
                        .map(OrderService::createTicketsByOrderResponse)
                        .collect(Collectors.toList());
            } else
                return new AppError(HttpStatus.LOCKED.value(), "Заказ уже был использован", orderEntity.getDateChanged());
        }
        return new AppError(HttpStatus.NOT_FOUND.value(), "Заказ не найден");
    }

    public Object getTicketsByOrder(OrderEntity orderEntity) {
            if (!orderEntity.getStatus().equals(STATUS_USED)) {
                List<OrderItemEntity> orderItemEntities = orderItemsRepository.getOrderItemEntitiesByOrderId(orderEntity.getOrderId());
                return orderItemEntities.stream()
                        .map(OrderService::createTicketsByOrderResponse)
                        .collect(Collectors.toList());
            } else
                return new AppError(HttpStatus.LOCKED.value(), "Заказ уже был использован", orderEntity.getDateChanged());
    }

    @Transactional
    public Optional<CouponEntity> addNewCoupon(HttpEntity<String> httpEntity) {
        Optional<CouponEntity> addedCoupon = Optional.empty();
        Optional<CreateCouponRequest> couponRequest = Converter.jsonToObject(httpEntity.getBody(), CreateCouponRequest.class);
        if (couponRequest.isPresent()) {
            CouponEntity newCoupon = couponEntityMapper(couponRequest.get());
            CouponEntity returnedCoupon = couponsRepository.save(newCoupon);
            addedCoupon = Optional.of(returnedCoupon);
        }
        return addedCoupon;
    }

    @Transactional
    public Optional<InstitutionTicketEntity> addNewInstitutionTicket(HttpEntity<String> httpEntity) {
        Optional<InstitutionTicketEntity> addedIte = Optional.empty();
        Optional<CreateInstitutionTicketToRemote> request = Converter.jsonToObject(httpEntity.getBody(), CreateInstitutionTicketToRemote.class);

        if (request.isPresent()) {
            CreateInstitutionTicketToRemote req = request.get();
            InstitutionTicketEntity oldIte = institutionTicketsRepository.findInstitutionTicketEntityByTicket_TicketId(req.getTicketId());

            if (oldIte != null) {
                updateOldInstitutionTicketEntity(oldIte, req);
            } else {
                oldIte = institutionTicketEntityMapper(req);
            }

            addedIte = Optional.of(institutionTicketsRepository.save(oldIte));
        }

        return addedIte;
    }

    private void updateOldInstitutionTicketEntity(InstitutionTicketEntity oldIte, CreateInstitutionTicketToRemote req) {
        oldIte.setInstitutionId(req.getInstitutionId());
        oldIte.setTicket(ticketsRepository.findTicketEntityByTicketId(req.getTicketId()));
        oldIte.setPrice(req.getPrice());
        oldIte.setIsActive(true);
    }


    @Transactional
    public ResponseEntity<?> addNewOrder(HttpEntity<String> order) {
        Optional<CreateOrderRequest> orderRequest = Converter.jsonToObject(order.getBody(), CreateOrderRequest.class);

        if (orderRequest.isEmpty()) {
            return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), "Отправлены неверные данные"));
        }

        CreateOrderRequest or = orderRequest.get();

        if (or.getSum() < 0) {
            return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), "Сумма заказа не может быть отрицательной"));
        }

        UUID institutionId = allTicketsHaveSameInstitutionId(or);
        if (institutionId == null) {
            return ResponseEntity.badRequest().body(new AppError(HttpStatus.BAD_REQUEST.value(), "Все билеты должны иметь одинаковый institutionId"));
        }

        try {
            PaidPersonEntity paidPerson = getOrCreatePaidPerson(or.getName(), or.getEmail());
            OrderEntity newOrder = createAndSaveOrder(or.getSum(), institutionId, paidPerson);
            saveOrderItems(newOrder.getOrderId(), Arrays.asList(or.getTickets()));
            sendTicketEmail(newOrder.getOrderId().toString(), or.getEmail(), or.getName());

            return ResponseEntity.ok("Ticket sent successfully.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ошибка при создании и отправке билета"));
        }
    }

    @Transactional
    public TicketsByTypeResponse[] getTickets(UUID institution) {
        try {
            List<TypeEntity> typesList = typesRepository.findAll();

            return typesList.stream()
                    .map(type -> {
                        TicketsByTypeResponse ticketsByTypeResponse = new TicketsByTypeResponse();
                        ticketsByTypeResponse.setType(type.getName());
                        ticketsByTypeResponse.setDescription(type.getDescription());

                        List<InstitutionTicketEntity> institutionTicketsEntities = institutionTicketsRepository.findByIsActiveTrueAndTicket_TypeAndInstitutionId(type, institution);

                        if (!institutionTicketsEntities.isEmpty()) {
                            List<InstitutionTicket> ticketsList = institutionTicketsEntities.stream()
                                    .map(institutionTicketEntity -> {
                                        TicketEntity ticketEntity = institutionTicketEntity.getTicket();
                                        TimeEntity timeEntity = ticketEntity.getTime();
                                        return new InstitutionTicket(
                                                institutionTicketEntity.getInstitutionTicketId(),
                                                timeEntity != null ? timeEntity.getMinutes() : null,
                                                institutionTicketEntity.getPrice()
                                        );
                                    })
                                    .toList();
                            ticketsByTypeResponse.setInstitutionTickets(ticketsList.toArray(new InstitutionTicket[0]));
                        }
                        return ticketsByTypeResponse;
                    })
                    .toArray(TicketsByTypeResponse[]::new);
        } catch (Exception e) {
            System.err.println("Ошибка во время формирования TicketsByTypeResponse: " + e.getMessage());
            return new TicketsByTypeResponse[0];
        }
    }

    public CouponEntity[] getCoupons() {
        return couponsRepository.findAll().toArray(new CouponEntity[0]);
    }

    public Optional<Integer> getSaleByCouponCode(String couponCode, UUID institution) {
        if (couponCode == null) return Optional.empty();
        if (institution == null) return Optional.empty();
        Optional<CouponEntity> couponEntityOptional = Optional.ofNullable(couponsRepository.findTopByCouponCodeOrderByExpirationDateDesc(couponCode, institution));
        if (couponEntityOptional.isPresent()) {
            CouponEntity ce = couponEntityOptional.get();
            ce.setUsed(ce.getUsed() + 1);
            couponsRepository.save(ce);
        }
        return couponEntityOptional.map(CouponEntity::getDiscountAmount);
    }

    public Object getOrder(UUID id) {
        Optional<OrderEntity> orderEntity = ordersRepository.getOrderEntityByOrderId(id);
        if (orderEntity.isPresent())
            return orderEntity.get();
        else
            return new AppError(HttpStatus.NOT_FOUND.value(), "Заказ не найден");
    }

    //    utils

    private static TicketsByOrderResponse createTicketsByOrderResponse(OrderItemEntity orderItemEntity) {
        TicketEntity te = orderItemEntity.getInstitutionTicketEntity().getTicket();
        String ticketType = te.getType().getName() + " " + Converter.convertMinutesToHours(te.getTime().getMinutes());
        return new TicketsByOrderResponse(ticketType, orderItemEntity.getOrderItemId(), orderItemEntity.getInstitutionTicketEntity().getInstitutionTicketId(), orderItemEntity.getTicketsCount());
    }

    private OrderItemEntity orderItemEntityMapper(UUID orderId, InstituteTicketCartItemRequest instituteTicketCartItemRequest) {
        InstitutionTicketEntity ite = institutionTicketsRepository.getInstitutionTicketEntityByInstitutionTicketId(instituteTicketCartItemRequest.getInstitutionTicketId());
        return new OrderItemEntity(orderId, ite, instituteTicketCartItemRequest.getCount());
    }

    private CouponEntity couponEntityMapper(CreateCouponRequest ccr) {
        return new CouponEntity(institutionsRepository.getInstitutionId(), ccr.getCode(), ccr.getDiscountAmount(), ccr.getExpirationDate(), ccr.getDescription());
    }

    private InstitutionTicketEntity institutionTicketEntityMapper(CreateInstitutionTicketToRemote request) {
        return new InstitutionTicketEntity(request.getInstitutionId(), ticketsRepository.findTicketEntityByTicketId(request.getTicketId()), request.getPrice());
    }

    private UUID allTicketsHaveSameInstitutionId(CreateOrderRequest or) {
        UUID institutionId = null;
        for (InstituteTicketCartItemRequest ticket : or.getTickets()) {
            if (institutionId == null) {
                institutionId = institutionTicketsRepository.getInstitutionTicketEntityByInstitutionTicketId(ticket.getInstitutionTicketId()).getInstitutionId();
            } else if (!institutionId.equals(institutionTicketsRepository.getInstitutionTicketEntityByInstitutionTicketId(ticket.getInstitutionTicketId()).getInstitutionId())) {
                return null;
            }
        }
        return institutionId;
    }

    private PaidPersonEntity getOrCreatePaidPerson(String name, String email) {
        Optional<PaidPersonEntity> person = paidPersonRepository.getPaidPersonEntityByEmail(email);
        if (person.isPresent()) {
            return person.get();
        } else {
            PaidPersonEntity newPerson = new PaidPersonEntity(name, email);
            paidPersonRepository.save(newPerson);
            return newPerson;
        }
    }

    private OrderEntity createAndSaveOrder(double sum, UUID institutionId, PaidPersonEntity paidPersonEntity) {
        OrderEntity newOrder = new OrderEntity(statusesRepository.getStatusEntityByName("paid"), institutionId, sum, paidPersonEntity);
        return ordersRepository.save(newOrder);
    }

    private void saveOrderItems(UUID orderId, List<InstituteTicketCartItemRequest> tickets) {
        for (InstituteTicketCartItemRequest ticket : tickets) {
            OrderItemEntity newOrderItem = orderItemEntityMapper(orderId, ticket);
            orderItemsRepository.save(newOrderItem);
        }
    }

    private void sendTicketEmail(String orderId, String email, String name) throws Exception {
        byte[] ticketData = printService.createTicket(orderId);
        emailService.sendEmailWithAttachment(email, ticketData, "ticket.pdf");
//        mailerSender.sendEmail(email, name, ticketData);
    }
}
