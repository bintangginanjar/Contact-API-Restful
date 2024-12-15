package com.com.apirestful.mapper;

import com.com.apirestful.entity.AddressEntity;
import com.com.apirestful.model.AddressResponse;

public class AddressResponseMapper {

    public static AddressResponse ToAddressResponse(AddressEntity address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

}
