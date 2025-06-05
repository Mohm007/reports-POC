package org.example.report.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WebDataMetric {
    private String month;
    private int visitors;
}