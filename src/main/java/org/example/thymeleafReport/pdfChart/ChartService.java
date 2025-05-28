package org.example.thymeleafReport.pdfChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class ChartService {

    // Method to create a bar chart
    public JFreeChart createBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Category1", "A");
        dataset.addValue(4.0, "Category1", "B");
        dataset.addValue(3.0, "Category1", "C");
        dataset.addValue(5.0, "Category1", "D");

        return ChartFactory.createBarChart(
                "Sample Bar Chart",
                "Category",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
    }

    // Method to create a pie chart
    public JFreeChart createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Category A", 50.0);
        dataset.setValue("Category B", 30.0);
        dataset.setValue("Category C", 20.0);

        return ChartFactory.createPieChart3D(
                "Sample Pie Chart",
                dataset,
                true,
                true,
                false);
    }

    // Method to create a line chart
    public JFreeChart createLineChart() {
        XYSeries series = new XYSeries("Line Chart");
        series.add(1, 1);
        series.add(2, 2);
        series.add(3, 3);
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        return ChartFactory.createXYLineChart(
                "Sample Line Chart",
                "X",
                "Y",
                dataset);
    }

    // Method to convert a chart to Base64
    public String getChartImageBase64(JFreeChart chart) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(chart.createBufferedImage(600, 400), "png", byteArrayOutputStream);
        byte[] chartBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(chartBytes);
    }

    public String convertChartToBase64(BufferedImage chartImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
