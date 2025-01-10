package net.springboot.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.springboot.model.User;
import net.springboot.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	Object getresult();
	User save(Double result);
	User save(Double result, Long id);
	Object getresult(String id);
	User save(User user);
	Object getall();
}
