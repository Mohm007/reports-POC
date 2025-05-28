package org.example.RM;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.NetworkDevice;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class Report {

    private String reportName;
    private String reportType;  // e.g. "CSV", "PDF"
    private ChartType chartType;  // e.g. BAR, PIE, LINE
    private LocalDateTime reportTime;
    private String description;
    private List<NetworkDevice> networkDevices;  // Data from JSON

    // Constructors, getters and setters

    public Report(String reportName, String reportType, ChartType chartType, LocalDateTime reportTime, String description, List<NetworkDevice> networkDevices) {
        this.reportName = reportName;
        this.reportType = reportType;
        this.chartType = chartType;
        this.reportTime = reportTime;
        this.description = description;
        this.networkDevices = networkDevices;
    }

    // Getters and setters

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<NetworkDevice> getNetworkDevices() {
        return networkDevices;
    }

    public void setNetworkDevices(List<NetworkDevice> networkDevices) {
        this.networkDevices = networkDevices;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportName='" + reportName + '\'' +
                ", reportType='" + reportType + '\'' +
                ", chartType=" + chartType +
                ", reportTime=" + reportTime +
                ", description='" + description + '\'' +
                ", networkDevices=" + networkDevices +
                '}';
    }
}
