package org.example.report.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportData {
    private String reportName;
    private String reportDate;
    private String footer;
    private List<Metric> metrics;
}
