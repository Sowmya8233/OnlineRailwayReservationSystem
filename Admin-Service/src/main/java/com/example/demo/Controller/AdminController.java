package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Repository.AdminRepository;
import com.example.demo.Service.AdminService;
import com.example.demo.Service.JwtFilterReq;
import com.example.demo.model.AdminContact;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.util.JwtUtils;



@RestController
@RequestMapping("/adminlogin")
public class AdminController {

	
	@Autowired AdminRepository Adminrepo;
	
	@Autowired RestTemplate restTemplate;
	@Autowired JwtUtils jwtutil;
	
	@Autowired AdminService Adminservice;
	
	@Autowired AuthenticationManager authenticates;
	
	@PostMapping("/signup")
	private ResponseEntity<AuthenticationResponse>subscribeClient(@RequestBody AuthenticationRequest authreq){
		AdminContact admin =new AdminContact();
		
		admin.setUsername(authreq.getUsername());
		admin.setPassword(authreq.getPassword());
		admin.setRole("ADMIN");
		
		try {
			Adminrepo.save(admin);
		}
		catch(Exception e){
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse
					("Error during subscription ") , HttpStatus.OK);
		}
		
		return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse
				("Successful subs for client " +authreq.getUsername()), HttpStatus.OK);

	}
	
	
	@PostMapping("/auth")
	private ResponseEntity<?> authenticateClient(@RequestBody AuthenticationRequest authreq){
		
		String username=authreq.getUsername();
		String password= authreq.getPassword();
		
		try {
			authenticates.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				
		}
		catch(Exception e) {
			return ResponseEntity.ok(new AuthenticationResponse(" Invalid Credentials..!"));
		}
		
		UserDetails userdetails= Adminservice.loadUserByUsername(username);
		
		String jwt = jwtutil.generateToken(userdetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
//	@PostMapping("/add")
//	public String add(@RequestBody AdminContact contact) {
//		contact.setRole("ADMIN");
//	Adminrepo.save(contact);
//	return "Added contact with id :  " + contact.getId();
//			
//}
	
	@GetMapping("/findAllDetails") 
	public List<AdminContact> getContacts(){
		List<AdminContact> lstadminContact = Adminrepo.findAll();
		lstadminContact.forEach((i) -> i.setPassword(""));
		return lstadminContact;
		
	}
	@GetMapping("/findAllDetails/{id}")
	public List<AdminContact> getContact(@PathVariable String id){
		List<AdminContact> lstadminContact = Adminrepo.findAll();
		lstadminContact.forEach((i) -> i.setPassword(""));
		return lstadminContact;
	}
	
	 @DeleteMapping("/delcontact/{id}")
		public String deleteContact (@PathVariable String id) {
			Adminrepo.deleteById(id);
			return "Contact deleted with id : "+id;
		}
	 
//	 @GetMapping("/loadUserByUsername/{username}")
//	 public UserDetails loadUserByUsername(@PathVariable String username){
//			AdminContact foundedUser=Adminrepo.findByusername(username);
//			if (foundedUser==null) {
//				return null;
//			}
//			String user=foundedUser.getUsername();
//			String pass=foundedUser.getPassword();
//			return new User(user, pass,new ArrayList<>());
//		}
	 
	@GetMapping("/addTrain")
	public String Addtrains(@RequestBody Object addtrain) {
		String response = this.restTemplate
		.postForObject("http://localhost:9004/trains/addTrain", addtrain,String.class );
		System.out.println(response);
		return response;
	}
	
	@GetMapping("Train/{trainid}")
	public Object getTrainById(@PathVariable String trainid){
		Object response = this.restTemplate
		.getForObject("http://localhost:9004/trains/"+ trainid,Object.class );
		System.out.println(response);
		return response;
	}
	

	@GetMapping("/delete/{trainid}")
	public String deleteTrain(@PathVariable String trainid) {
		String response = this.restTemplate
				.getForObject("http://localhost:9004/trains/delete/"+ trainid,String.class );
				System.out.println(response);
		return response;
		
	}
	
	@GetMapping("/update/{trainid}")
	public Object updateTrain(@PathVariable("trainid") String trainid,@RequestBody Object train ) {
		Object response = this.restTemplate
				.postForObject("http://localhost:9004/trains/update/"+ trainid,train,Object.class );
				System.out.println(response);
		return response;
		
	}
	 
	}



