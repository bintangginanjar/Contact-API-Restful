package com.com.apirestful.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.com.apirestful.entity.AddressEntity;
import com.com.apirestful.entity.ContactEntity;
import com.com.apirestful.entity.UserEntity;
import com.com.apirestful.mapper.AddressResponseMapper;
import com.com.apirestful.model.AddressResponse;
import com.com.apirestful.model.CreateAddressRequest;
import com.com.apirestful.model.UpdateAddressRequest;
import com.com.apirestful.repository.AddressRepository;
import com.com.apirestful.repository.ContactRepository;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired 
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    public AddressService(ContactRepository contactRepository, AddressRepository addressRepository, ValidationService validationService) {
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.validationService = validationService;
    }

    @Transactional
    public AddressResponse create(UserEntity user, CreateAddressRequest request) {
        validationService.validate(request);

        Integer contactId = 0;

        try {
            contactId = Integer.parseInt(request.getContactId());       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        
        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        AddressEntity address = new AddressEntity();
        //address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);

        return AddressResponseMapper.ToAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public AddressResponse get(UserEntity user, String strContactId, String strAddressId) {        
        Integer contactId = 0;
        Integer addressId = 0;

        try {
            contactId = Integer.parseInt(strContactId);
            addressId = Integer.parseInt(strAddressId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        AddressEntity address = addressRepository.findFirstByContactAndId(contact, addressId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));
        
        return AddressResponseMapper.ToAddressResponse(address);        
    }

    @Transactional
    public AddressResponse update(UserEntity user, UpdateAddressRequest request) {
        validationService.validate(request);

        Integer contactId = 0;
        Integer addressId = 0;

        try {
            contactId = Integer.parseInt(request.getContactId());
            addressId = Integer.parseInt(request.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        AddressEntity address = addressRepository.findFirstByContactAndId(contact, addressId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        
        return AddressResponseMapper.ToAddressResponse(address);
    }

    @Transactional
    public void delete(UserEntity user, String strContactId, String strAddressId) {
        Integer contactId = 0;
        Integer addressId = 0;

        try {
            contactId = Integer.parseInt(strContactId);
            addressId = Integer.parseInt(strAddressId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        AddressEntity address = addressRepository.findFirstByContactAndId(contact, addressId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

        addressRepository.delete(address);        
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(UserEntity user, String strContactId) {
        Integer contactId = 0;        

        try {
            contactId = Integer.parseInt(strContactId);            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        List<AddressEntity> addresses = addressRepository.findAllByContact(contact);

        List<AddressResponse> addressResponses = addresses.stream()
                                                .map(p -> new AddressResponse(
                                                            p.getId(), 
                                                            p.getStreet(), 
                                                            p.getCity(), 
                                                            p.getProvince(), 
                                                            p.getCountry(), 
                                                            p.getPostalCode()))
                                                .collect(Collectors.toList());

        return addressResponses;
        //return addresses.stream().map(AddressResponseMapper.ToAddressResponse(addresses)).toList();
    }
}
