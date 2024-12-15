package com.com.apirestful.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.com.apirestful.entity.ContactEntity;
import com.com.apirestful.entity.UserEntity;
import com.com.apirestful.mapper.ContactResponseMapper;
import com.com.apirestful.model.ContactResponse;
import com.com.apirestful.model.CreateContactRequest;
import com.com.apirestful.model.SearchContactRequest;
import com.com.apirestful.model.UpdateContactRequest;
import com.com.apirestful.repository.ContactRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    public ContactService(ContactRepository contactRepository, ValidationService validationService) {
        this.contactRepository = contactRepository;
        this.validationService = validationService;
    }

    @Transactional
    public ContactResponse create(UserEntity user, CreateContactRequest request) {
        validationService.validate(request);

        ContactEntity contact = new ContactEntity();
        contact.setFirstname(request.getFirstname());
        contact.setLastname(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return ContactResponseMapper.ToContactResponse(contact);

        /*
        return ContactResponse.builder()
                .id(contact.getId())
                .firstname(contact.getFirstname())
                .lastname(contact.getLastname())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
         */
    }

    @Transactional(readOnly = true)
    public ContactResponse get(UserEntity user, String strContactId) {
        Integer contactId = 0;

        try {
            contactId = Integer.parseInt(strContactId);       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return ContactResponseMapper.ToContactResponse(contact);

        /*
        return ContactResponse.builder()
                .id(contact.getId())
                .firstname(contact.getFirstname())
                .lastname(contact.getLastname())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
        */
    }

    @Transactional
    public ContactResponse update(UserEntity user, UpdateContactRequest request) {
        validationService.validate(request);

        Integer contactId = 0;    

        try {
            contactId = Integer.parseInt(request.getId());       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstname(request.getFirstname());
        contact.setLastname(request.getLastname());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return ContactResponseMapper.ToContactResponse(contact);

        /*
        return ContactResponse.builder()
                .id(contact.getId())
                .firstname(contact.getFirstname())
                .lastname(contact.getLastname())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
        */
    }

    @Transactional
    public void delete(UserEntity user, String strContactId) {     
        Integer contactId = 0;    

        try {
            contactId = Integer.parseInt(strContactId);       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        ContactEntity contact = contactRepository.findFirstByUserAndId(user, contactId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contactRepository.delete(contact);
    }

    @SuppressWarnings("null")
    @Transactional(readOnly = true)    
    public Page<ContactResponse> search(UserEntity user, SearchContactRequest request) {
        Specification<ContactEntity> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                    builder.like(root.get("firstname"), "%"+request.getName()+"%"),
                    builder.like(root.get("lastname"), "%"+request.getName()+"%")
                ));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%"+request.getEmail()+"%"));
            }

            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%"+request.getPhone()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<ContactEntity> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts
                                                    .getContent()
                                                    .stream()
                                                    .map(contact -> ContactResponseMapper.ToContactResponse(contact))
                                                    .collect(Collectors.toList());

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
