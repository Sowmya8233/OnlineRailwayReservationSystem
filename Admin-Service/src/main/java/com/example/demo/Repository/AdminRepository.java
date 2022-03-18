package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.AdminContact;


public interface AdminRepository extends MongoRepository<AdminContact, String> {

	
	AdminContact findByusername(String username);
}
