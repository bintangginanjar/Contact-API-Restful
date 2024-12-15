package com.com.apirestful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.com.apirestful.entity.AddressEntity;
import com.com.apirestful.entity.ContactEntity;
import java.util.List;


public interface AddressRepository extends JpaRepository<AddressEntity, Integer>{

    Optional<AddressEntity> findFirstByContactAndId(ContactEntity contact, Integer addressId);

    List<AddressEntity> findAllByContact(ContactEntity contact);
}
