package org.example.report.highcharts;

import org.example.report.model.Metric;
import org.example.report.model.ReportData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

@Component
public class HighchartsExporter {

//    @Value("${chart.export.server.url:http://localhost:7801}")
    @Value("${chart.export.server.url:http://localhost:8889}")      // LOCAL DOCKER RUNNING PORT
    private String exportServerUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public void generateAllChartImages(ReportData reportData) throws Exception {
        for (Metric metric : reportData.getMetrics()) {
            if ("TABULAR".equalsIgnoreCase(metric.getChartType())) continue;

            JSONObject chartConfig = HighchartsChartConfigBuilder.buildConfig(metric);

            JSONObject payload = new JSONObject();
            payload.put("infile", chartConfig.toString());
            payload.put("type", "image/png");
            payload.put("scale", 0.9);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);

            ResponseEntity<byte[]> response = restTemplate.postForEntity(
                    exportServerUrl + "/export",
                    requestEntity,
                    byte[].class
            );

            byte[] chartBytes = response.getBody();

            String fileName = "chart-" + UUID.randomUUID() + ".png";
            File outputFile = new File("src/main/resources/static/charts/" + fileName);
            outputFile.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(chartBytes);
            }

            String base64 = Base64.getEncoder().encodeToString(chartBytes);
            metric.setChartImageWebPath("data:image/png;base64," + base64);

            String fileUrl = outputFile.toURI().toURL().toString();
            metric.setChartImageFileUri(fileUrl);

            System.out.println("Chart image URI: " + fileUrl);

        }
    }
}
