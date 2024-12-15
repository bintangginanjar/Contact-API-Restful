package com.com.apirestful.model;

import jakarta.validation.constraints.Email;
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
public class CreateContactRequest {

    @NotBlank
    @Size(max = 128)
    private String firstname;

    @Size(max = 128)        
    private String lastname;

    @Email
    @Size(max = 128)        
    private String email;

    @Size(max = 128)        
    private String phone;
}
