package net.springboot.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.springboot.model.Files;
import net.springboot.model.User;
import net.springboot.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
	Object getresult();
	User save(Double result);
	Files savefiles(Files file);
	Long getid() ;
	List<Files> getAllfiles();
	
	void deletefile(String name);
	
}
