package org.example.report.utils;

import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;

public class PdfRenderer {
    public static void renderPdfToStream(String html, OutputStream os) throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(os);
    }
}
