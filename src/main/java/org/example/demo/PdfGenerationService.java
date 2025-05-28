package org.example.demo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class PdfGenerationService {

    public byte[] generatePDF() throws IOException {
        // Load devices from JSON file
        List<NetworkDevice> devices = loadDevicesFromJson();

        PDDocument document = new PDDocument();

        // A4 size dimensions (in points)
        float pageWidth = 595.2767f;
        float pageHeight = 841.8898f;

        PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));  // A4 size
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Initial Y position for content
        float yStart = 750f;
        float yPosition = yStart;
        float margin = 50f;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float rowHeight = 20f;
        int rowsPerPage = 25; // Change this value based on the content height
        int currentRow = 0;

        // Load logo image from the URL
        URL logoUrl = new URL("https://yourstrategic.imgix.net/2017/04/Extreme-Networks-Logo.png?fm=png&ixlib=php-3.3.1.png");
        PDImageXObject logo = PDImageXObject.createFromFileByContent(new File(logoUrl.getFile()), document);

        // Logo size
        float logoWidth = 100;
        float logoHeight = 30;

        // Draw logo in the top-right corner of the header
        contentStream.drawImage(logo, page.getMediaBox().getWidth() - logoWidth - margin, yPosition);
        yPosition -= logoHeight + 10; // Move the Y position after the logo

        // Draw the header for the table (with borders)
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Device Name");
        contentStream.newLineAtOffset(100, 0);  // Adjust column width
        contentStream.showText("Site");
        contentStream.newLineAtOffset(100, 0);  // Adjust column width
        contentStream.showText("Building");
        contentStream.newLineAtOffset(100, 0);  // Adjust column width
        contentStream.showText("MAC Address");
        contentStream.newLineAtOffset(100, 0);  // Adjust column width
        contentStream.showText("Model");
        contentStream.endText();

        // Draw horizontal line after header
        yPosition -= 15;
        contentStream.setLineWidth(1f);
        contentStream.beginText();
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(page.getMediaBox().getWidth() - margin, yPosition);
        contentStream.stroke();
        yPosition -= 5;

        // Write table content and handle pagination
        for (NetworkDevice device : devices) {
            if (currentRow == rowsPerPage) {
                // Add a new page if needed
                page = new PDPage(new PDRectangle(pageWidth, pageHeight));  // A4 size page
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);

                // Draw header again on the new page
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Device Name");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Site");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Building");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("MAC Address");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Model");
                contentStream.endText();

                // Draw horizontal line after header
                yPosition -= 15;
                contentStream.setLineWidth(1f);
                contentStream.beginText();
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(page.getMediaBox().getWidth() - margin, yPosition);
                contentStream.stroke();
                yPosition -= 5;

                currentRow = 0; // Reset row count after a new page
            }

            // Write data for the current row
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(device.getHostname());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(device.getSite());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(device.getBuilding());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(device.getMacAddress());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(device.getModel());
            contentStream.endText();

            // Move to the next row
            yPosition -= rowHeight;
            currentRow++;

            // Check if we need to add a new page
            if (yPosition < margin) {
                // Add a new page if content goes beyond the page
                page = new PDPage(new PDRectangle(pageWidth, pageHeight));  // A4 size page
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = 750f;
            }
        }

        contentStream.close();

        // Convert the document to a byte array and return it
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }




    public byte[] generatePlainPDF() {
        List<NetworkDevice> devices = loadDevicesFromJson();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Load image from URL
            String logoUrl = "https://yourstrategic.imgix.net/2017/04/Extreme-Networks-Logo.png?fm=png&ixlib=php-3.3.1.png";
            URL url = new URL(logoUrl);
            InputStream inputStream = url.openStream();

            // Load the image from the input stream
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, inputStream.readAllBytes(), "logo");

            // Create a content stream to write the content to the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Draw the logo in the header section (adjust X, Y coordinates accordingly)
            float logoX = 50;  // X position of the logo
            float logoY = 750; // Y position of the logo (adjust as needed)
            float logoWidth = 100;  // Width of the logo
            float logoHeight = 50;  // Height of the logo
            contentStream.drawImage(pdImage, logoX, logoY, logoWidth, logoHeight);

            // Header section
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 750);  // Starting point for the header
            contentStream.showText("Network Devices Report"); // Report Title
            contentStream.endText();  // Close header text block

            float yPosition = 700;  // Start position for the table rows
            float margin = 50;
            float tableWidth = 500;
            float rowHeight = 20f;
            float cellMargin = 5f;
            float cellWidth = tableWidth / 5;

            // Draw table headers
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.newLineAtOffset(margin, yPosition);
            String[] headers = {"Hostname", "Site", "Building", "MAC Address", "Model"};
            for (String header : headers) {
                contentStream.showText(header);
                contentStream.newLineAtOffset(cellWidth, 0);
            }
            contentStream.endText();  // Close header text block

            // Move yPosition down after headers
            yPosition -= rowHeight;

            // Draw rows from the device list
            contentStream.setFont(PDType1Font.HELVETICA, 10); // Regular font for content
            for (NetworkDevice device : devices) {
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(device.getHostname());
                contentStream.newLineAtOffset(cellWidth, 0);
                contentStream.showText(device.getSite());
                contentStream.newLineAtOffset(cellWidth, 0);
                contentStream.showText(device.getBuilding());
                contentStream.newLineAtOffset(cellWidth, 0);
                contentStream.showText(device.getMacAddress());
                contentStream.newLineAtOffset(cellWidth, 0);
                contentStream.showText(device.getModel());
                contentStream.endText();  // Close each row text block
                yPosition -= rowHeight;

                if (yPosition < 100) {  // If the space is too small, create a new page
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage();  // Create a new page
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    yPosition = 750; // Reset Y position for the new page
                }
            }

            // Optionally add footer
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(margin, 50);
            contentStream.showText("Page 1");  // Example footer
            contentStream.endText();

            contentStream.close();

            // Write the document to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private List<NetworkDevice> loadDevicesFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(getClass().getClassLoader().getResource("networkDevices.json").toURI());
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, NetworkDevice.class));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

}
