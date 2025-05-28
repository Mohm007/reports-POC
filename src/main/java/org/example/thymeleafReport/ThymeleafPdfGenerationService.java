package org.example.thymeleafReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class ThymeleafPdfGenerationService {

    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generatePdf(String htmlContent, Context context) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        // Apply the Thymeleaf template (Ensure template content is valid HTML)
        String htmlWithData = templateEngine.process("report", context);

        // Convert HTML to PDF
        renderer.setDocumentFromString(htmlWithData);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

}
