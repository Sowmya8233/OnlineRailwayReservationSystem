package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.AdminRepository;
import com.example.demo.model.AdminContact;


@Service
public class AdminService implements UserDetailsService {

	
	@Autowired
	
	public AdminRepository Adminrepo;
	
	public AdminContact addContact (AdminContact contact) {
		return Adminrepo.save(contact);
	}

	public List<AdminContact> getContact() {
		List<AdminContact> contact = Adminrepo.findAll();
		System.out.println("Getting data from DB : " + contact);
		return contact;
	}

	public Optional<AdminContact> getContactbyId(String id) {
		return Adminrepo.findById(id);
	}

	public void deleteContact(AdminContact contact) {
		Adminrepo.delete(contact);
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AdminContact foundedUser=Adminrepo.findByusername(username);
		if (foundedUser==null) {
			return null;
		}
		String user=foundedUser.getUsername();
		String pass=foundedUser.getPassword();
		return new User(user, pass,new ArrayList<>());
	}
	
}
