package org.example.report.highcharts;

import org.example.report.model.Metric;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class HighchartsChartConfigBuilder {

    public static JSONObject buildConfig(Metric metric) {
        String chartType = metric.getChartType().toLowerCase();
        List<String> headers = metric.getHeaders();
        List<List<Object>> data = metric.getData();

        JSONObject chart = new JSONObject().put("type", chartType);
        JSONObject title = new JSONObject().put("text", metric.getTitle());
        JSONObject config = new JSONObject()
                .put("chart", chart)
                .put("title", title);

        // Line/Bar charts expect xAxis and series
        if ("line".equals(chartType) || "bar".equals(chartType)) {
            JSONArray categories = new JSONArray();
            JSONArray dataPoints = new JSONArray();

            for (List<Object> row : data) {
                categories.put(row.get(0).toString());
                dataPoints.put(row.get(1));
            }

            JSONObject xAxis = new JSONObject().put("categories", categories);
            JSONObject seriesItem = new JSONObject()
                    .put("name", headers.get(1))
                    .put("data", dataPoints);

            config.put("xAxis", xAxis);
            config.put("series", new JSONArray().put(seriesItem));

        } else if ("pie".equals(chartType)) {
            JSONArray pieData = new JSONArray();

            for (List<Object> row : data) {
                JSONObject point = new JSONObject()
                        .put("name", row.get(0).toString())
                        .put("y", Double.parseDouble(row.get(1).toString()));
                pieData.put(point);
            }

            JSONObject seriesItem = new JSONObject()
                    .put("name", headers.get(1))
                    .put("colorByPoint", true)
                    .put("data", pieData);

            config.put("series", new JSONArray().put(seriesItem));
        }

        return config;
    }
}
