package com.photoapp.users.serviceImpl;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.photoapp.users.dtos.UsersDto;
import com.photoapp.users.entity.Users;
import com.photoapp.users.repositories.UsersRepository;
import com.photoapp.users.service.UsersService;
@Service
public class UsersServiceImpl implements UsersService {

	UsersRepository usersRepository;
	//PasswordEncoder passwordEncoder;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//@Autowired
	public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = passwordEncoder;
		
	}
	
	@Override
	public UsersDto createUsers(UsersDto usersDto) {
		usersDto.setPublicUserId(UUID.randomUUID().toString());
		usersDto.setEncryptedPassword(this.bCryptPasswordEncoder.encode(usersDto.getEncryptedPassword()));
		ModelMapper modelMapper = new ModelMapper();
		//Used to match fields between DTO and Entity Strictly
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); 
		Users users = modelMapper.map(usersDto, Users.class);
		//users.setEncryptedPassword("test");
		this.usersRepository.save(users);
		UsersDto result = modelMapper.map(users, UsersDto.class);
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users = this.usersRepository.findByEmail(username);
		if (users == null)
			throw new UsernameNotFoundException(username);
		
		return new User(users.getEmail(), users.getEncryptedPassword(), true, true, true,
				true, new ArrayList<>());
	}

	@Override
	public UsersDto getUserDetailsByEmail(String email) {
		Users users = this.usersRepository.findByEmail(email);
		if (users == null)
			throw new UsernameNotFoundException(email);
		return new ModelMapper().map(users, UsersDto.class);
	}

}
