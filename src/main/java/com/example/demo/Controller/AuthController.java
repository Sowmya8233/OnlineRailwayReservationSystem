package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import com.example.demo.model.*;

import com.example.demo.utils.JwtUtils;


@RestController
@RequestMapping("/login")
public class AuthController {


	@Autowired
	   RestTemplate restTemplate;
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	AuthenticationManager authenticates;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	JwtUtils jwtutil;
	
	@PostMapping("/subs")
	private ResponseEntity<AuthenticationResponse>subscribeClient(@RequestBody AuthenticationRequest authreq){
		UserModel usermodel =new UserModel();
		
		usermodel.setUsername(authreq.getUsername());
		usermodel.setPassword(authreq.getPassword());
		
		
		try {
			userrepo.save(usermodel);
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
		
		UserDetails userdetails= userservice.loadUserByUsername(username);
		
		String jwt = jwtutil.generateToken(userdetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
//	@GetMapping("/test")
//	private String testingtoken() {
//		try {
//			return "Testing Successful...!";	
//		}
//		catch(Exception e) {
//			return "Please login first..!";
//		}
//	}
//	
//	@GetMapping("/dashboard")
//	private String dashboard() {
//		return "Welcome to dashboard...!";
//	}

	@GetMapping("/Booking")
	public Object AddBooking(@RequestBody Object Booking) {
		Object response = this.restTemplate
		.postForObject("http://localhost:9003/Booking/addBooking", Booking,Object.class );
		System.out.println(response);
		return response;
	}

	@GetMapping("/findbyid/{id}")
	public Object getEmployee(@PathVariable String id ) {
		return this.restTemplate.getForObject("http://localhost:9003/Booking/"+id,
				Object.class);
	}

}