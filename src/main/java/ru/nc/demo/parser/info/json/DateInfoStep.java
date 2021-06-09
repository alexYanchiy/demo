package ru.nc.demo.parser.info.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateInfoStep {

    @JsonProperty(value = "payload")
    private DateInfoStepPayload payload;

    public DateInfoStepPayload getPayload() {
        return payload;
    }
    public void setPayload(DateInfoStepPayload payload) {
        this.payload = payload;
    }
}
