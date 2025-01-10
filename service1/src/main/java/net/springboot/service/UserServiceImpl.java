package net.springboot.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.springboot.model.Files;
import net.springboot.model.Role;
import net.springboot.model.User;
import net.springboot.repository.FilesRepository;
import net.springboot.repository.UserRepository;
import net.springboot.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;
	private FilesRepository fileRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private BCryptPasswordEncoder numberEncoder;
	@Autowired
	private BCryptPasswordEncoder codeEncoder;

	
	public UserServiceImpl(UserRepository userRepository,FilesRepository fileRepository) {
		super();
		this.userRepository = userRepository;
		this.fileRepository = fileRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
	String 	ps=passwordEncoder.encode(registrationDto.getPassword());
	String 	ps1=numberEncoder.encode(registrationDto.getPassword());
	String 	ps2=codeEncoder.encode(registrationDto.getPassword());
		User user = new User(registrationDto.getFirstName(), 
				registrationDto.getLastName(), registrationDto.getEmail(),
				ps, ps1,ps2, Arrays.asList(new Role("ROLE_USER")));
		
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User save(Double result) {
	   Long id =	getid() ;
		Optional<User> optional = userRepository.findById(id);
		User user = null;
		if (optional.isPresent()) {
			user = optional.get();
		} else {
			throw new RuntimeException(" Employee not found for id :: " + id);
		}
		user.setResult(result);
		return userRepository.save(user);
	}
	@Override
	public Long getid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String me= authentication.getName() ;
		List<User> a = userRepository.findAll() ;
		int i=0;  User b=null ;
		while ( i < a.size()) {
			b= a.get(i) ;
			if(b.getEmail().equals(me)) {
				return b.getId() ;	
			}
		i++;
		}
		
		return b.getId() ;
	}
	@Override
	public Object getresult() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String use = authentication.getName() ;
		List<User> a = userRepository.findAll() ;
		int i=0;User b=null ;
		while ( i < a.size()) {
			b= a.get(i) ;
			if(b.getEmail()==use) {
				i=a.size()+1;
			}
		i++;	
		}
		
		return userRepository.save(b);
	}
	@Override
	public Files savefiles(Files file) {

		
		return fileRepository.save(file);
	}

	@Override
	public List<Files> getAllfiles() {
		List<Files> a = fileRepository.findAll();
		int i=0; Files b = null;
		String id =String.valueOf(getid()) ; int j = 0;
		while ( i < a.size()) {
		 if  ( ! id.equals(a.get(i).getUserId()))	{
			a.remove(i);
			i--;
		 }
		i++;	
		}
		return a ;
	}

	@Override
	public void deletefile(String name) {
		List<Files> a = fileRepository.findAll() ;
		int i=0;  Files b=null ;
		while ( i < a.size()) {
			b= a.get(i) ;
			if(b.getName().equals(name)) {
				Long id = b.getId() ;
				fileRepository.deleteById(id);;
			}
		i++;
		}
		
		
		
	}

}
