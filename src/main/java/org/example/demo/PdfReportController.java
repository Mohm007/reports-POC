package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/report")
public class PdfReportController {

    private static final float MARGIN = 50;
    private static final float Y_START = 750;
    private static final float TABLE_WIDTH = 500;
    private static final float CELL_HEIGHT = 20;
    private static final float CELL_PADDING = 5;
    private static final float FONT_SIZE = 12;
    private static final float TABLE_HEIGHT = 700;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdfReport(HttpServletResponse response) {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePDF();  // No parameters needed
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "inline; filename=network_device_report.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @GetMapping("/generate-pdf")
//    public ResponseEntity<byte[]> generatePdfReport(HttpServletResponse response) {
//        try {
//            byte[] pdfBytes = pdfGenerationService.generatePDF();
////            byte[] pdfBytes = pdfGenerationService.generateTestPDF();
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Type", "application/pdf");
//            headers.add("Content-Disposition", "inline; filename=network_device_report.pdf");
//            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();  // Log the error
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/generate-plain-pdf")
    public ResponseEntity<byte[]> generatePlainPdfReport(HttpServletResponse response) {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePlainPDF();
//            byte[] pdfBytes = pdfGenerationService.generateTestPDF();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "inline; filename=network_device_report.pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/generate")
    public void generatePdf(HttpServletResponse response) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);

        // Draw the header
        drawHeader(contentStream);

        // Draw Table 1
        float yStart = drawTable(contentStream, 650, "Table 1: Sample Data", new String[]{"Row", "Data"});

        // Draw Table 2
        yStart = drawTable(contentStream, yStart - 20, "Table 2: More Data", new String[]{"Row", "Additional Data"});

        // Draw Footer
        drawFooter(contentStream, page, document, 1);

        contentStream.close();
        document.save(response.getOutputStream());
        document.close();

        // Set content type and headers for PDF download
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf");
        response.flushBuffer();
    }

    private void drawHeader(PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE);
        contentStream.newLineAtOffset(MARGIN, Y_START);
        contentStream.showText("REPORT ON NETWORK DEVICES");
        contentStream.endText();
    }

    private float drawTable(PDPageContentStream contentStream, float yStart, String tableTitle, String[] columnTitles) throws IOException {
        // Draw title for the table
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE);
        contentStream.newLineAtOffset(MARGIN, yStart);
        contentStream.showText(tableTitle);
        contentStream.endText();

        // Draw the table columns and rows
        yStart -= 30;  // Leave space for the title
        float yPosition = yStart;
        float cellMargin = 5f;
        float xPosition = MARGIN;

        // Draw header row
        contentStream.setLineWidth(1f);
        float tableWidth = TABLE_WIDTH;
        float cellHeight = CELL_HEIGHT;

        // Draw header borders
        contentStream.moveTo(xPosition, yPosition);
        contentStream.lineTo(xPosition + tableWidth, yPosition);
        contentStream.stroke();

        // Add text to the header row
        contentStream.beginText();
        contentStream.newLineAtOffset(xPosition + cellMargin, yPosition + 5);
        contentStream.showText(columnTitles[0]);
        contentStream.newLineAtOffset(tableWidth / 2, 0);
        contentStream.showText(columnTitles[1]);
        contentStream.endText();

        yPosition -= cellHeight;

        // Draw data rows
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
        for (int i = 0; i < 10; i++) {
            // Draw borders for each cell in the row
            contentStream.moveTo(xPosition, yPosition);
            contentStream.lineTo(xPosition + tableWidth, yPosition);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition + cellMargin, yPosition + 5);
            contentStream.showText("Row " + (i + 1));
            contentStream.newLineAtOffset(tableWidth / 2, 0);
            contentStream.showText("Data " + (i + 1));
            contentStream.endText();

            yPosition -= cellHeight;

            // Draw borders for the bottom of the row
            contentStream.moveTo(xPosition, yPosition);
            contentStream.lineTo(xPosition + tableWidth, yPosition);
            contentStream.stroke();
        }
        return yPosition;
    }


    private void drawFooter(PDPageContentStream contentStream, PDPage page, PDDocument document, int pageNumber) throws IOException {
        // Draw Footer with pagination
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
        contentStream.newLineAtOffset(MARGIN, 50);
        contentStream.showText("Page " + pageNumber);
        contentStream.endText();
    }

}
