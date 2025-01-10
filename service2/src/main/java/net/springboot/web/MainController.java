package net.springboot.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import net.springboot.model.Binary;

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
		model.addAttribute("user", userService.getresult());
		return "result";
	}
	 @PostMapping("/convertFile")
	    public String uploadFile(
	            @RequestParam("file") MultipartFile multipartFile )
	                    throws IOException, Exception {
	         
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        long size = multipartFile.getSize();
	        Path uploadPath = Paths.get("");
	          
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }
	 
	    
	         
	        try (InputStream inputStream = multipartFile.getInputStream()) {
	            Path filePath = uploadPath.resolve( fileName);
	            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException ioe) {       
	            throw new IOException("Could not save file: " + fileName, ioe);
	        }
	         
	        
	        
	        
	        Document pdfDoc = new Document(PageSize.A4);
	        PdfWriter.getInstance(pdfDoc, new FileOutputStream("C:\\Users\\Bsi\\projet\\service3\\result.pdf"))
	          .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	        pdfDoc.open() ;
	        
	        Font myfont = new Font();
	        myfont.setStyle(Font.NORMAL);
	        myfont.setSize(11);
	        pdfDoc.add(new Paragraph("\n"));
	         
	        BufferedReader br = new BufferedReader (new FileReader(fileName)  );
	        String strLine;
	        while ((strLine = br.readLine()) != null) {
	            Paragraph para = new Paragraph(strLine + "\n", myfont);
	            para.setAlignment(Element.ALIGN_JUSTIFIED);
	            pdfDoc.add(para);
	        }	
	        pdfDoc.close();
	        br.close(); 
	         
	 
	       
	        return  "redirect:/download/result.pdf/"+fileName ;
	    }
	 @GetMapping("/download/{fileCode}/{filename}")
	    public ResponseEntity<?>downlaod(@PathVariable("fileCode") String fileCode,@PathVariable("filename") String filename) {
	        FileDownloadUtil downloadUtil = new FileDownloadUtil();
	         
	        Resource resource = null;
	        try {
	            resource = downloadUtil.getFileAsResource(fileCode);
	        } catch (IOException e) {
	            
	        }
	         
	        if (resource == null) {
	            
	        }
	         
	        String contentType = "application/octet-stream";
	        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
	        File f= new File("C:\\Users\\Bsi\\projet\\service3\\"+filename);  
	     	 f.delete();
	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
	                .body(resource);     
	        
	     	
	    }
}
