package com.com.apirestful.mapper;

import com.com.apirestful.entity.ContactEntity;
import com.com.apirestful.model.ContactResponse;

public class ContactResponseMapper {

    public static ContactResponse ToContactResponse(ContactEntity contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstname(contact.getFirstname())
                .lastname(contact.getLastname())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();        
    }

}
