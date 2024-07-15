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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class PrintService {

    public byte[] createTicket(String data) throws Exception {

        BufferedImage qrCodeImage = generateQRCodeImage(data);
        ClassPathResource resource = new ClassPathResource("ticket.pdf");

        try (InputStream inputStream = resource.getInputStream();
             PDDocument document = PDDocument.load(inputStream);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {


            PDPage page = document.getPage(0);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                insertQrCode(document, contentStream, qrCodeImage, 130, 182, 245, 245);
            }
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private void insertQrCode(PDDocument document, PDPageContentStream contentStream, BufferedImage qrCodeImage,
                              float x, float y, float width, float height) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        PDImageXObject pdImage = LosslessFactory.createFromImage(document, ImageIO.read(bais));

        Matrix matrix = new Matrix(width / qrCodeImage.getWidth(), 0, 0, height / qrCodeImage.getHeight(), x, y);
        contentStream.transform(matrix);
        contentStream.drawImage(pdImage, 0, 0);
    }
}
