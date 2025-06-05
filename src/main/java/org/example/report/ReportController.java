package org.example.report;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report-engine")
public class ReportController {

    private final ReportGenerator reportGenerator;

    public ReportController(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @GetMapping("/html")
    public ResponseEntity<String> getHtmlReport() throws Exception {
        String html = reportGenerator.generateHtmlReport();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getPdfReport() throws Exception {
        byte[] pdfBytes = reportGenerator.generatePdfReport();

        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
