package org.example.RM;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ChartRenderingService {

    // Generate a chart image (e.g., PNG)
    public File generateChartImage(String chartType, String[] labels, int[] data) throws IOException {
        JFreeChart chart;

        // Dataset creation based on chart type
        switch (chartType.toUpperCase()) {
            case "PIE":
                chart = createPieChart(labels, data);
                break;
            case "LINE":
                chart = createLineChart(labels, data);
                break;
            case "BAR":
            default:
                chart = createBarChart(labels, data);
                break;
        }

        // Render the chart to a BufferedImage
        BufferedImage bufferedImage = chart.createBufferedImage(600, 400);

        // Save the BufferedImage to a file
        File chartFile = new File("chart.png");
        ImageIO.write(bufferedImage, "PNG", chartFile);

        return chartFile;
    }

    // Pie chart generation
    private JFreeChart createPieChart(String[] labels, int[] data) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (int i = 0; i < labels.length; i++) {
            dataset.setValue(labels[i], data[i]);
        }

        return ChartFactory.createPieChart("Pie Chart", dataset, true, true, false);
    }

    // Line chart generation
    private JFreeChart createLineChart(String[] labels, int[] data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < labels.length; i++) {
            dataset.addValue(data[i], "Category", labels[i]);
        }

        return ChartFactory.createLineChart("Line Chart", "Category", "Value", dataset);
    }

    // Bar chart generation
    private JFreeChart createBarChart(String[] labels, int[] data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < labels.length; i++) {
            dataset.addValue(data[i], "Category", labels[i]);
        }

        return ChartFactory.createBarChart("Bar Chart", "Category", "Value", dataset, PlotOrientation.VERTICAL, true, true, false);
    }
}
