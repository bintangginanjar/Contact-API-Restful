package com.com.apirestful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.com.apirestful.entity.UserEntity;
import com.com.apirestful.model.AddressResponse;
import com.com.apirestful.model.CreateAddressRequest;
import com.com.apirestful.model.UpdateAddressRequest;
import com.com.apirestful.model.WebResponse;
import com.com.apirestful.service.AddressService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE            
    )
    public WebResponse<AddressResponse> create(UserEntity user,
                                               @RequestBody CreateAddressRequest request,
                                               @PathVariable("contactId") String contactId) {
        request.setContactId(contactId);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @GetMapping(
        path = "/api/contacts/{contactId}/addresses/{addressId}",
        produces = MediaType.APPLICATION_JSON_VALUE        
    )
    public WebResponse<AddressResponse> get(UserEntity user,                                               
                                               @PathVariable("contactId") String contactId,
                                               @PathVariable("addressId") String addressId) {

        AddressResponse addressResponse = addressService.get(user, contactId, addressId);

        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @PutMapping(
        path = "/api/contacts/{contactId}/addresses/{addressId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(UserEntity user, 
                                @RequestBody UpdateAddressRequest request, 
                                @PathVariable("contactId") String contactId,
                                @PathVariable("addressId") String addressId) {
        
        request.setId(addressId);
        request.setContactId(contactId);

        AddressResponse addressResponse = addressService.update(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }
    
    @DeleteMapping(
        path = "/api/contacts/{contactId}/addresses/{addressId}",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(UserEntity user,                                 
                                @PathVariable("contactId") String contactId,
                                @PathVariable("addressId") String addressId) {

        addressService.delete(user, contactId, addressId);

        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
        path = "/api/contacts/{contactId}/addresses",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> delete(UserEntity user,                                 
                                @PathVariable("contactId") String contactId){

        List<AddressResponse> addressResponses = addressService.list(user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
    }
    
}
