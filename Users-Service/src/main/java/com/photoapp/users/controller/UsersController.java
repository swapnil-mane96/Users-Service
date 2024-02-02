package com.photoapp.users.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photoapp.users.dtos.UsersDto;
import com.photoapp.users.model.CreateUserRequests;
import com.photoapp.users.model.NewUserResponse;
import com.photoapp.users.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private Environment environment; //used to see port number of multiple instances of a service
	
	@Autowired
	private UsersService usersService;
	
	@GetMapping
	public String status() {
		return "Users Microservice is Working Fine... "+environment.getProperty("local.server.port");
	}
	
	@PostMapping("/createusers")
	public ResponseEntity<NewUserResponse> createUsers(@RequestBody CreateUserRequests createUserRequests){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UsersDto usersDto = modelMapper.map(createUserRequests, UsersDto.class);
		
		UsersDto registeredUser = this.usersService.createUsers(usersDto);
		
		NewUserResponse newUserResponse = modelMapper.map(registeredUser, NewUserResponse.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newUserResponse);
		
	}
}
