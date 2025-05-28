package org.example.thymeleafReport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.NetworkDevice;
import org.example.demo.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/pdf")
public class ThymeleafController {

    @Autowired
    private ThymeleafPdfGenerationService pdfGenerationService;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePdf() throws Exception {

        // Load devices data from JSON
        List<NetworkDevice> devices = loadDevicesFromJson();  // 'devices' is more descriptive
        Context context = new Context();
        context.setVariable("devices", devices);  // Use 'devices' as the name

        // Create the HTML content with Thymeleaf
        String htmlContent = templateEngine.process("report", context);

        // Generate the PDF bytes from the HTML content
        byte[] pdfBytes = pdfGenerationService.generatePdf(htmlContent, context);

        // Create response headers for the PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("report.pdf").build());

        // Return the generated PDF as a response
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
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

