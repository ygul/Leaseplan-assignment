
package com.assignment.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "speed",
        "deg"
})
public class Wind {

    @JsonProperty("speed")
    private Double speed;
    @JsonProperty("deg")
    private Integer deg;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
