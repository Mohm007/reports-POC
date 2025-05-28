package org.example.RM;

public enum ChartType {
    BAR("Bar Chart"),
    PIE("Pie Chart"),
    LINE("Line Chart");

    private final String description;

    ChartType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
