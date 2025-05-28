package org.example.thymeleafReport.pdfChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/charts")
public class PdfChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping("/pdf")
    @ResponseBody
    public void generatePdf(HttpServletResponse response) throws IOException {
        // Generate Base64 chart images
        String barChartBase64 = chartService.getChartImageBase64(chartService.createBarChart());
        String pieChartBase64 = chartService.getChartImageBase64(chartService.createPieChart());
        String lineChartBase64 = chartService.getChartImageBase64(chartService.createLineChart());

        // Prepare HTML content with embedded Base64 charts
        String htmlContent = buildHtmlContent(barChartBase64, pieChartBase64, lineChartBase64);

        // Set response to return a PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=charts.pdf");

        try {
            // Generate PDF using Flying Saucer
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);  // Use HTML content for rendering
            renderer.layout();
            renderer.createPDF(response.getOutputStream());
        } catch (Exception e) {
            // Log exception details for debugging
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while generating the PDF.");
        }
    }

    // Helper method to generate HTML content
    private String buildHtmlContent(String barChartBase64, String pieChartBase64, String lineChartBase64) {
        return "<html>" +
                "<head><style>img {max-width: 100%; height: auto;}</style></head>" +
                "<body>" +
                "<h1>Charts PDF</h1>" +
                "<h2>Bar Chart</h2>" +
                "<img src='data:image/png;base64," + barChartBase64 + "'/>" +
                "<h2>Pie Chart</h2>" +
                "<img src='data:image/png;base64," + pieChartBase64 + "'/>" +
                "<h2>Line Chart</h2>" +
                "<img src='data:image/png;base64," + lineChartBase64 + "'/>" +
                "</body>" +
                "</html>";
    }
}
