package org.example.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkDevice {


    private String hostname;
    private String site;
    private String building;
    private String macAddress;
    private String model;

    public NetworkDevice(String hostname, String site, String building, String macAddress, String model) {
        this.hostname = hostname;
        this.site = site;
        this.building = building;
        this.macAddress = macAddress;
        this.model = model;
    }

    // Default constructor (optional)
    public NetworkDevice() {
    }
}

