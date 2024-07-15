package ru.kikopark.backend.modules.order.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kikopark.backend.modules.order.dto.OrderAndTicketsResponse;
import ru.kikopark.backend.modules.order.dto.OrderDto;
import ru.kikopark.backend.modules.order.dto.TicketsByOrderResponse;
import ru.kikopark.backend.modules.order.dto.TicketsByTypeResponse;
import ru.kikopark.backend.modules.order.entities.*;
import ru.kikopark.backend.utils.AppError;
import ru.kikopark.backend.modules.order.service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class OrderController {
    OrderService orderService;

    @PostMapping("/guest/something")
    public ResponseEntity<?> createSomething(HttpEntity<String> httpEntity) {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/site/coupon")
    public ResponseEntity<Integer> getSaleByCouponCode(@RequestParam String code, @RequestParam UUID institution) {
        Optional<Integer> discountAmount = orderService.getSaleByCouponCode(code, institution);
        return discountAmount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/site/tickets")
    public ResponseEntity<TicketsByTypeResponse[]> getTickets(@RequestParam UUID institution) {
        TicketsByTypeResponse[] response = orderService.getTickets(institution);
        if (response.length != 0)
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/localserver/order-and-tickets")
    public ResponseEntity<?> getOrderAndTickets(@RequestParam UUID id) {
        Object response = orderService.getOrder(id);
        if (response instanceof OrderEntity orderEntity) {
            Object ticketsResponse = orderService.getTicketsByOrder(orderEntity);
            if (ticketsResponse instanceof List<?> ticketsResponseList) {
                TicketsByOrderResponse[] tickets = ticketsResponseList.stream()
                        .filter(TicketsByOrderResponse.class::isInstance)
                        .map(TicketsByOrderResponse.class::cast)
                        .toArray(TicketsByOrderResponse[]::new);
                return new ResponseEntity<>(new OrderAndTicketsResponse(orderEntityToDto(orderEntity), tickets), HttpStatus.OK);
            } else {
                return AppError.process(response);
            }
        } else {
            return AppError.process(response);
        }
    }

    private OrderDto orderEntityToDto(OrderEntity orderEntity) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderEntity.getOrderId());
        orderDto.setSum(orderEntity.getSum());
        orderDto.setInstitutionId(orderEntity.getInstitutionId());
        orderDto.setStatus(orderEntity.getStatus().getName());
        orderDto.setDatePaid(orderEntity.getDatePaid());
        orderDto.setDateChanged(orderEntity.getDateChanged());
        return orderDto;
    }


    @GetMapping("/localserver/order")
    public ResponseEntity<?> getOrder(@RequestParam UUID id) {
        Object response = orderService.getOrder(id);
        return (response instanceof OrderEntity) ?
                new ResponseEntity<>(response, HttpStatus.OK) :
                AppError.process(response);
    }

    @GetMapping("/localserver/tickets-by-order")
    public ResponseEntity<?> getTicketsByOrder(@RequestParam UUID id) {
        Object response = orderService.getTicketsByOrder(id);
        return (response instanceof List<?>) ?
                new ResponseEntity<>(response, HttpStatus.OK) :
                AppError.process(response);
    }

    @GetMapping("/admin/coupons")
    public ResponseEntity<CouponEntity[]> getCoupons() {
        CouponEntity[] response = orderService.getCoupons();
        return (response.length != 0) ?
                new ResponseEntity<>(response, HttpStatus.OK) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/site/new-order")
    public ResponseEntity<?> addOrder(HttpEntity<String> httpEntity) {
        Object response = orderService.addNewOrder(httpEntity);
        return (response instanceof ResponseEntity<?> r) ?
                r :
                AppError.process(response);

    }

    @PostMapping("/admin/new-institution-ticket")
    public ResponseEntity<?> addInstitutionTicket(HttpEntity<String> httpEntity) {
        Optional<InstitutionTicketEntity> insertionSuccess = orderService.addNewInstitutionTicket(httpEntity);
        return (insertionSuccess.isPresent()) ?
                new ResponseEntity<>(insertionSuccess.get().getInstitutionId(), HttpStatus.OK) :
                new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Отправлены неправильные данные"), HttpStatus.BAD_REQUEST);
    }
}
