package org.example.report.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metric {

    private String metricName;
    private String title;
    private List<String> headers;
    private List<List<Object>> data;
    private String chartType;

    private String chartImagePath;
    private String chartImageFileUri;
    private String chartImageWebPath;
}