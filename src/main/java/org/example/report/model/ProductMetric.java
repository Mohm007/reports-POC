package org.example.report.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductMetric {
    private String product;
    private int units_sold;
}