package com.example.login.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.login.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByEmail(String email);

  
}
