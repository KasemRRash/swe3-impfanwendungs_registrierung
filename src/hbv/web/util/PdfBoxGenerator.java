package hbv.web.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfBoxGenerator implements PdfService {

  @Override
  public File generatePDF(String bookingId, String datum, String impfzentrum) throws IOException {
    File pdfFile = new File("/tmp/buchung_" + bookingId + ".pdf");

    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      // erst text am anfang hinzufügen
      try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(100, 750);
        contentStream.showText("Impfbestätigung");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Datum: " + datum);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Impfzentrum: " + impfzentrum);
        contentStream.endText();
      }

      // qrcode generieren
      BufferedImage qrCodeImage = null;
      try {
        qrCodeImage = generateQRCode(bookingId);
      } catch (WriterException e) {
        System.err.println("Fehler beim erstellen vom qr code: " + e.getMessage());
      }

      // wenn qr erfolgreich erstellt wurde, einfügen
      if (qrCodeImage != null) {
        PDImageXObject qr =
            PDImageXObject.createFromByteArray(document, toByteArray(qrCodeImage), "qr-code");
        try (PDPageContentStream Stream =
            new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
          Stream.drawImage(qr, 100, 500, 200, 200); // position: (100,500), Größe: 200x200
        }
      }

      document.save(pdfFile);
    }

    return pdfFile;
  }

  // methode für qrcode
  private BufferedImage generateQRCode(String text) throws WriterException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  // konvertiere BufferedImage in Bytearray für pdfbox
  private byte[] toByteArray(BufferedImage image) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(image, "png", baos);
    return baos.toByteArray();
  }
}
