package org.example.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.report.highcharts.HighchartsExporter;
import org.example.report.model.ReportData;
import org.example.report.utils.PdfRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Controller
public class HighchartsReportController {

    private final PdfRenderer pdfRenderer;
    private final HighchartsExporter chartExporter;
    private final ObjectMapper objectMapper;

    @Value("${report.data.path:data/metrics.json}")
    private String dataFilePath;

    public HighchartsReportController(
            PdfRenderer pdfRenderer,
            HighchartsExporter chartExporter,
            ObjectMapper objectMapper
    ) {
        this.pdfRenderer = pdfRenderer;
        this.chartExporter = chartExporter;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/highcharts-report")
    public ResponseEntity<byte[]> generateHighchartsPdf() throws Exception {
        // 1. Load JSON data
        InputStream is = new ClassPathResource(dataFilePath).getInputStream();
        ReportData reportData = objectMapper.readValue(is, ReportData.class);

        // 2. Export all charts using Highcharts export server
        chartExporter.generateAllChartImages(reportData);

        // 3. Generate HTML from Thymeleaf
        String html = pdfRenderer.renderHtml("highcharts-pdf-template", reportData);

        // 4. Convert HTML to PDF
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        pdfRenderer.renderPdfToStream(html, pdfStream);

        // 5. Return as downloadable response
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=highcharts-report.pdf")
                .body(pdfStream.toByteArray());
    }
}
