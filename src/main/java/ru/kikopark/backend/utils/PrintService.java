package ru.kikopark.backend.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class PrintService {

    public static void createTicket(String data) throws Exception {
        BufferedImage qrCodeImage = generateQRCodeImage(data);
        // Загрузка существующего PDF-документа
        PDDocument document = PDDocument.load(new File("src/main/java/ru/kikopark/backend/utils/ticket.pdf"));
        // Получение первой страницы документа (вы можете выбрать другую, если нужно)
        PDPage page = document.getPage(0);
        // Получение контент-стрима для добавления содержимого
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
        // Вставка QR-кода на существующую страницу
        insertQrCode(document, contentStream, qrCodeImage, 130, 182, 245, 245);
        // Закрытие потока контента
        contentStream.close();
        // Сохранение изменений в существующем PDF-документе
        document.save("src/main/java/ru/kikopark/backend/utils/output_document" + data + ".pdf");
        // Закрытие документа
        document.close();
    }

    private static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static void insertQrCode(PDDocument document, PDPageContentStream contentStream, BufferedImage qrCodeImage,
                                     float x, float y, float width, float height) throws IOException {
        // Преобразование BufferedImage в PDImageXObject
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, ImageIO.read(bais));

        // Настройка матрицы преобразования для вставки изображения
        Matrix matrix = new Matrix(width / qrCodeImage.getWidth(), 0, 0, height / qrCodeImage.getHeight(), x, y);
        contentStream.transform(matrix);

        // Вставка изображения в PDF
        contentStream.drawImage(pdImage, 0, 0);
    }
}
