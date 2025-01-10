package net.springboot.web;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


 
public class FileDownloadUtil {
	 private Path foundFile;
     
	    public Resource getFileAsResource(String fileCode) throws IOException {
	        Path dirPath = Paths.get("C:\\Users\\Bsi\\projet\\service3\\");
	         
	        Files.list(dirPath).forEach(file -> {
	            if (file.getFileName().toString().startsWith(fileCode)) {
	                foundFile = file;
	                return;
	            }
	        });
	 
	        if (foundFile != null) {
	            return new UrlResource(foundFile.toUri());
	        }
	         
	        return null;
	    }
   
}