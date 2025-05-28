package org.example.thymeleafReport.charts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.NetworkDevice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pdf")
public class ChartController {

    @GetMapping("/chart")
    public String generateReport(Model model) {
        // Load data from JSON
        List<NetworkDevice> devices = loadDevicesFromJson();

        // Device count per site
        Map<String, Integer> deviceCounts = devices.stream()
                .collect(Collectors.groupingBy(NetworkDevice::getSite, Collectors.summingInt(e -> 1)));

        // Device count per model
        Map<String, Integer> deviceModels = devices.stream()
                .collect(Collectors.groupingBy(NetworkDevice::getModel, Collectors.summingInt(e -> 1)));

        // Create line chart data (site over index)
        List<Map<String, Object>> lineChartData = deviceCounts.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("site", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        model.addAttribute("lineChartData", lineChartData);


        // Pass data to template
        model.addAttribute("deviceCounts", deviceCounts.entrySet().stream()
                .map(entry -> Map.of("name", entry.getKey(), "y", entry.getValue()))
                .collect(Collectors.toList()));

        model.addAttribute("deviceModels", deviceModels.entrySet().stream()
                .map(entry -> Map.of("key", entry.getKey(), "value", entry.getValue()))
                .collect(Collectors.toList()));

        model.addAttribute("devices", devices);

        return "chart";
    }

    private List<NetworkDevice> loadDevicesFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(getClass().getClassLoader().getResource("networkDevices.json").toURI());
            return objectMapper.readValue(file, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, NetworkDevice.class));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file", e);
        }
    }
}
