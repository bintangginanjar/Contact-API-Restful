package com.com.apirestful.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAddressRequest {

    @NotBlank
    @JsonIgnore
    private String contactId;

    @NotBlank
    @JsonIgnore
    private String id;

    @Size(max = 128)
    private String street;

    @Size(max = 128)
    private String city;

    @Size(max = 128)
    private String province;
    
    @NotBlank
    @Size(max = 128)
    private String country;

    @Size(max = 10)
    private String postalCode;

}
