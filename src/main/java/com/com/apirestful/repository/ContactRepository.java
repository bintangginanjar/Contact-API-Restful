package com.com.apirestful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.com.apirestful.entity.ContactEntity;
import com.com.apirestful.entity.UserEntity;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Integer>, 
                                            JpaSpecificationExecutor<ContactEntity> {

    Optional<ContactEntity> findFirstByUserAndId(UserEntity user, Integer id);

    Optional<ContactEntity> findFirstByUser(UserEntity user);

}
