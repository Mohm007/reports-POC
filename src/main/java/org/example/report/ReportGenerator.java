package org.example.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.report.model.Metric;
import org.example.report.model.ReportData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.example.report.utils.PdfRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
public class ReportGenerator {

    private final TemplateEngine templateEngine;

    public ReportGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public ReportData loadReportData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(
                new File("src/main/resources/data/metrics.json"),
                ReportData.class
        );
    }

    public void generateChartImage(Metric metric, int index) throws Exception {
        String chartType = metric.getChartType();
        JFreeChart chart = null;

        if ("PIE".equalsIgnoreCase(chartType)) {
            // PIE chart
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            for (List<Object> row : metric.getData()) {
                String key = (String) row.get(0);
                double value = Double.parseDouble(row.get(1).toString());
                dataset.setValue(key, value);
            }

            chart = ChartFactory.createPieChart(
                    metric.getTitle(),
                    dataset,
                    true,
                    true,
                    false
            );

        } else if ("BAR".equalsIgnoreCase(chartType)) {
            // BAR chart (vertical)
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (List<Object> row : metric.getData()) {
                String category = (String) row.get(0);
                Number value = Double.parseDouble(row.get(1).toString());
                dataset.addValue(value, metric.getTitle(), category);
            }

            chart = ChartFactory.createBarChart(
                    metric.getTitle(),
                    "Category",   // domain axis label
                    "Value",      // range axis label
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,  // legend
                    true,  // tooltips
                    false  // URLs
            );

        } else if ("LINE".equalsIgnoreCase(chartType)) {
            // LINE chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (List<Object> row : metric.getData()) {
                String category = (String) row.get(0);
                Number value = Double.parseDouble(row.get(1).toString());
                dataset.addValue(value, metric.getTitle(), category);
            }

            chart = ChartFactory.createLineChart(
                    metric.getTitle(),
                    "Category",   // domain axis label
                    "Value",      // range axis label
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,  // legend
                    true,  // tooltips
                    false  // URLs
            );

        } else if("TABULAR".equalsIgnoreCase(chartType)){
            // TABULAR chart type
            System.out.println("Generated TABULAR Data type");
            return;
        } else{
            // Unsupported chart type
            System.out.println("Unsupported chart type: " + chartType);
            return;
        }

        // Improve rendering quality
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        File outputDir = new File("src/main/resources/data/static/images");
        if (!outputDir.exists()) {
            Files.createDirectories(outputDir.toPath());
        }

        File outputFile = new File(outputDir, "chart_" + index + ".png");
        ChartUtils.saveChartAsPNG(outputFile, chart, 600, 400);  // Adjust size as needed

        String absolutePath = outputFile.getAbsolutePath().replace("\\", "/");
        String fileUri = "file:///" + absolutePath;
        metric.setChartImageFileUri(fileUri);

        String webPath = "/images/chart_" + index + ".png";
        metric.setChartImageWebPath(webPath);

        System.out.println("Generated " + chartType + " chart image: " + outputFile.getAbsolutePath());
    }


    public String generateHtmlReport() throws Exception {
        ReportData reportData = loadReportData();

        // Generate images for PIE charts
        List<Metric> metrics = reportData.getMetrics();
        for (int i = 0; i < metrics.size(); i++) {
            generateChartImage(metrics.get(i), i);
        }

        Context context = new Context();
        context.setVariable("reportName", reportData.getReportName());
        context.setVariable("reportDate", reportData.getReportDate());
        context.setVariable("metrics", metrics);
        context.setVariable("footer", reportData.getFooter());

        return templateEngine.process("report-engine", context);
    }

    public byte[] generatePdfReport() throws Exception {
        String html = generateHtmlReport();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRenderer.renderPdfToStream(html, outputStream);
        return outputStream.toByteArray();
    }
}
