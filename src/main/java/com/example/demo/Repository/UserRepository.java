package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.UserModel;

public interface UserRepository extends MongoRepository<UserModel, String>{

	UserModel findByusername(String username);

}