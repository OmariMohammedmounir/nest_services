package net.springboot.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.springboot.model.Files;
import net.springboot.model.User;
import net.springboot.service.UserService;
import net.springboot.web.dto.UserRegistrationDto;


@Controller
public class MainController {
	public MainController(UserService userService) {
		super();
		this.userService = userService;
	}
	private UserService userService;
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	@PostMapping("/")
	public String convert( @RequestParam("number") String number) {
		
		
		double result=Double.valueOf(number);
	   String a =	Integer.toBinaryString((int) result) ; result=Double.valueOf(a);
		userService.save(result);
		return "redirect:/result";   
	
	}
	   
	@GetMapping("/result")
	public String showNewEmployeeForm(Model model) {
		// create model attribute to bind form data
		
		model.addAttribute("user", userService.getresult());
		return "result";
	}
	 @PostMapping("/uploadFile")
	    public String uploadFile(
	            @RequestParam("file") MultipartFile multipartFile )
	                    throws IOException {
	         
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        long size = multipartFile.getSize();
	        long id =   userService.getid();
	        
	        String filecode = FileUploadUtil.saveFile(fileName,String.valueOf(id), multipartFile);
	         
	        Files file = new Files(  fileName,String.valueOf(size),String.valueOf(id) );
	 
	        userService.savefiles(file);
	        FileUploadResponse response = new FileUploadResponse(); 
	        response.setFileName(fileName);
	        response.setSize(size);
	        response.setDownloadUri("/downloadFile/" + filecode);
	        new ResponseEntity<>(response, HttpStatus.OK);
	        return  "index" ;
	    }
	 @GetMapping("/downloadFile/{name}")
	    public ResponseEntity<?> downloadFile( @PathVariable("name") String name) {
	        FileDownloadUtil downloadUtil = new FileDownloadUtil();
	        long id =   userService.getid();
	        Resource resource = null;
	        try {
	            resource = downloadUtil.getFileAsResource(name,id);
	        } catch (IOException e) {
	        }
	         
	        if (resource == null) {
	            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
	        }
	         
	        String contentType = "application/octet-stream";
	        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
	         
	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
	                .body(resource);       
	    }
	 @GetMapping("/deleteFile/{id}/{name}")
	    public  String  deleteFile( @PathVariable("name") String name ,  @PathVariable("id") String id ,Model model) {
		 File f= new File("C:\\Users\\Bsi\\projet\\service2\\Files-Upload\\"+id+"\\"+name);  
	     	 f.delete();
	     	userService.deletefile(name);
	     	model.addAttribute("Files", userService.getAllfiles());
		 return "FilesList";
		 
	        
	    }
	 @GetMapping("/FilesList")
		public String listStudents(Model model) {
			model.addAttribute("Files", userService.getAllfiles());
			return "FilesList";
		}
}
