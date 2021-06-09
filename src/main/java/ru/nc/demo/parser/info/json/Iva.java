package ru.nc.demo.parser.info.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Iva {

    @JsonProperty(value = "DateInfoStep")
    private List<DateInfoStep> dateInfoStep;

    public List<DateInfoStep> getDateInfoStep() {
        return dateInfoStep;
    }
    public void setDateInfoStep(List<DateInfoStep> dateInfoStep) {
        this.dateInfoStep = dateInfoStep;
    }
}
