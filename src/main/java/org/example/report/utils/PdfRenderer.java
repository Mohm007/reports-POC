package org.example.report.utils;

import org.example.report.model.ReportData;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;

@Service
public class PdfRenderer {

    private final TemplateEngine templateEngine;

    public PdfRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Render Thymeleaf template with data to HTML string
     */
    public String renderHtml(String templateName, ReportData reportData) {
        Context context = new Context();
        context.setVariable("reportName", reportData.getReportName());
        context.setVariable("reportDate", reportData.getReportDate());
        context.setVariable("metrics", reportData.getMetrics());
        context.setVariable("footer", reportData.getFooter());  // if needed

        return templateEngine.process(templateName, context);
    }

    /**
     * Render the given HTML string to PDF and write to the OutputStream
     */
    public static void renderPdfToStream(String html, OutputStream os) throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(os);
        os.flush();
    }
}
