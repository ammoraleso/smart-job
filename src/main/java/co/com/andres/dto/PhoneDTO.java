package co.com.andres.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PhoneDTO {
    @JsonIgnore
    private Long id;
    private String number;
    @JsonProperty("citycode")
    private String cityCode;
    @JsonProperty("contrycode")
    private String countryCode;
}
