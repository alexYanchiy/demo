package ru.nc.demo.parser.info.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateInfoStepPayload {

    // 17 часов назад
    @JsonProperty(value = "relative")
    private String relative;

    // 28 февраля 11:05
    @JsonProperty(value = "absolute")
    private String absolute;

    public String getRelative() {
        return relative;
    }
    public void setRelative(String relative) {
        this.relative = relative;
    }
    public String getAbsolute() {
        return absolute;
    }
    public void setAbsolute(String absolute) {
        this.absolute = absolute;
    }
}
