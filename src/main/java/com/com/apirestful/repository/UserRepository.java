package com.com.apirestful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.com.apirestful.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findFirstByToken(String token);

}
