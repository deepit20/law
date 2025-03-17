package com.example.lawfirm.controller;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.lawfirm.model.Admin;
import com.example.lawfirm.model.AdminDto;
import com.example.lawfirm.model.Case;
import com.example.lawfirm.model.Doc;
import com.example.lawfirm.model.DocDto;
import com.example.lawfirm.service.AdminService;
import com.example.lawfirm.service.CaseService;
import com.example.lawfirm.service.ClientService;
import com.example.lawfirm.service.DocService;
import com.example.lawfirm.service.LawyerService;

import jakarta.validation.Valid;


@Controller
public class ClientController {
	@Autowired
    private AdminService adminService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private CaseService caseService;
	@Autowired
	private LawyerService lawyerService;
	@Autowired
	private DocService docService;
	
	
    public ClientController(AdminService adminService,ClientService cls,CaseService cs) { 
    	this.adminService = adminService; 
    	this.clientService=cls;
    	this.caseService=cs;
    }
    
    @GetMapping("/client")
    public String showClientDashboard() {
    	return "Client/client_dashboard";
    }
    
    @GetMapping("/client/cases")
    public String showCase(Model model) {
    	List<Case> cases = caseService.getAllCases();
    	
    	model.addAttribute("case", cases);
    	return "Client/registeredCase";
    }
    
    @GetMapping("/client/case/view/{id}")
    public String viewCase(@PathVariable Long id, Model model) {
        Optional<Case> caseOptional = caseService.getById(id);
        if (caseOptional.isPresent()) {
            Case caseDetails = caseOptional.get();
            List<Doc> caseDocuments = docService.findByCases(caseDetails); // Fetch only related docs
            model.addAttribute("caseDetails", caseDetails);
            DocDto docDto = new DocDto();
            docDto.setCases(null);
            model.addAttribute("doc", caseDocuments);
            model.addAttribute("docDto", docDto);
            return "Client/viewCase";
        }
        return "redirect:/client/cases"; // Redirect if case not found
    }

    
    @GetMapping("/client/case/upload/{id}")
    public String uploadDoc(Model model,@PathVariable Long id) {
    	DocDto docDto = new DocDto();
    	model.addAttribute("docDto", docDto);
    	Case cases = caseService.findById(id);
    	model.addAttribute("case", cases);
    	return "Client/uploadDoc";
    }
    
    @PostMapping("/client/case/upload/{id}")
    public String UploadDoc(@Valid @ModelAttribute DocDto docDto,BindingResult result,@PathVariable Long id) {
    	Case cases = caseService.findById(id);
    	//save file
    	MultipartFile doc = docDto.getDocFile();
    	if (doc.isEmpty()) {
            return "redirect:/client/case/upload/" + id;
        }
    	
    	String StorageFileName=cases.getCaseNumber()+"_"+doc.getOriginalFilename();
    	
    	try {
    		String uploadDir="public/documents/";
    		Path uploadPath=Paths.get(uploadDir);
    		
    		if(!Files.exists(uploadPath)) {
    			Files.createDirectories(uploadPath);
    		}
    		
    		try(InputStream inputStream=doc.getInputStream()){
    			Files.copy(inputStream, Paths.get(uploadDir + StorageFileName),
    					StandardCopyOption.REPLACE_EXISTING);
    		}
    	}catch(Exception ex) {
    		System.out.println("Exception: " + ex.getMessage());
    	}
        
    	
        
    	Doc docu=new Doc();
    	docu.setDocFileName(StorageFileName);
    	docu.setCases(cases);
    	
    	docService.save(docu);
    	
    	return "redirect:/client/case/view/"+cases.getId();
    }
    
    @GetMapping("/client/case/document/download/{docId}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadDocument(@PathVariable Long docId) {
        try {
            Doc doc = docService.getDocumentById(docId);
            String filePath = "public/documents/" + doc.getDocFileName();
            Path path = Paths.get(filePath);
            
            org.springframework.core.io.Resource resource = new UrlResource(path.toUri());
            String filename = doc.getDocFileName();
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/client/case/document/delete/{docId}/{caseId}")
    public String deleteDoc(Model model,@PathVariable long docId,@PathVariable long caseId,@ModelAttribute DocDto docDto,@ModelAttribute Case cases){
    	
    	try {
    		Doc doc = docService.getDocumentById(docId);
    		
    			//delete image
    			String uploadDir = "public/documents/";
    			Path oldImagePath = Paths.get(uploadDir + doc.getDocFileName());
    			
    			try {
    				Files.delete(oldImagePath);
    			}catch(Exception e) {
    				System.out.println("Exception: "+e.getMessage());
    			}
    			docService.delete(doc);
    	}catch(Exception ex) {
    		System.out.println("Exception: "+ex.getMessage());
    	}
    	
    	return "redirect:/client/case/view/"+caseId;
    }

}
