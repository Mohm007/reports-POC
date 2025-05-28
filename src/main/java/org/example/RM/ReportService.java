package org.example.RM;

import com.itextpdf.text.DocumentException;
import org.example.demo.NetworkDevice;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final TemplateEngine templateEngine;

    public ReportService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    // Generate the Report object
    public Report generateReport(String reportName, String reportType, ChartType chartType, String description) {
        // Example JSON data loading (you can replace this with actual data)
        List<NetworkDevice> networkDevices = loadDevicesFromJson();

        return new Report(
                reportName,
                reportType,
                chartType,
                LocalDateTime.now(),
                description,
                networkDevices
        );
    }

    // Generate PDF from Thymeleaf template
    public byte[] generatePdfFromReport(Report report) {
        // Create a context and add the model data to it
        Context context = new Context();
        context.setVariable("report", report);

        // Generate HTML content from the template
        String htmlContent = templateEngine.process("report-template", context);

        // Convert HTML to PDF using Flying Saucer
        return htmlToPdf(htmlContent);
    }

    // Convert HTML to PDF using Flying Saucer
    private byte[] htmlToPdf(String htmlContent) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            renderer.createPDF(os);
            return os.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    // Example method to load devices from JSON (you can replace this with actual data fetching)
    private List<NetworkDevice> loadDevicesFromJson() {
        // Your logic to read from JSON and return the list of devices
        return List.of(
                new NetworkDevice("SIM-098765", "San Jose", "HQ", "FFC1DE098765", "AP305"),
                new NetworkDevice("SIM-234567", "San Jose", "HQ", "FF7876234567", "AP5010")
        );
    }
}
