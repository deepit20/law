package com.example.lawfirm.controller;

import com.example.lawfirm.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;

import com.example.lawfirm.service.AdminService;
import com.example.lawfirm.service.CaseService;
import com.example.lawfirm.service.ClientService;
import com.example.lawfirm.service.LawyerService;

import jakarta.validation.Valid;

@Controller
public class LawyerController {
	@Autowired
    private AdminService adminService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private CaseService caseService;
	@Autowired
	private LawyerService lawyerService;
	
	
    public LawyerController(AdminService adminService,ClientService cls,CaseService cs) { 
    	this.adminService = adminService; 
    	this.clientService=cls;
    	this.caseService=cs;
    }
    
    @GetMapping("/lawyer")
    public String showLawyerDashboard(Model model) {
        return "Lawyer/lawyer_dashboard";
    }
    
    @GetMapping("/lawyer/cases")
    public String showCasesAssigned(Model model) {
    	List<Case> cases=caseService.getAllCases();
    	model.addAttribute("case", cases);
    	
    	List<Client> client = clientService.getAllClients();
    	model.addAttribute("client", client);
    	
    	return "Lawyer/assignedCase";
    }
    
    //profile section starts
    @GetMapping("/lawyer/profile")
    public String lawyerProfile(@RequestParam(value = "from", required = false, defaultValue = "dashboard") String from,
                                Model model, Principal principal) {
        // Get logged-in lawyer details
//        String email = principal.getName();
//        Lawyer lawyer = lawyerService.findByEmail(email);
    	List<Lawyer> lawyer=lawyerService.getAllLawyers();
        model.addAttribute("lawyer", lawyer);
        model.addAttribute("from", from);
        return "/lawyer/profile";
    }
    
    @GetMapping("/lawyer/profile/edit")
    public String showEditPage(Model model,@RequestParam Long id) {
    	
    	try {
    		Lawyer lawyer = lawyerService.findById(id);
    		model.addAttribute("lawyers", lawyer);
    		
    		LawyerDto lawyerDto = new LawyerDto();
    		lawyerDto.setFirstName(lawyer.getFirstName());
        	lawyerDto.setLastName(lawyer.getLastName());
        	lawyerDto.setDOB(lawyer.getDOB());
        	lawyerDto.setEmail(lawyer.getEmail());
        	lawyerDto.setPhone(lawyer.getPhone());
        	lawyerDto.setSpecialization(lawyer.getSpecialization());
        	
        	model.addAttribute("lawyerDto", lawyerDto);
    	}catch(Exception ex) {
    		System.out.println("Exception:"+ex.getMessage());
    		return "redirect:/lawyer/profile";
    	}
    	return "Lawyer/editProfile";
    }
    
    @PostMapping("/lawyer/profile/edit")
    public String updateLawyer(Model model,@RequestParam long id,@Valid @ModelAttribute LawyerDto lawyerDto,BindingResult result){
    	
    	try {
    		Lawyer lawyer = lawyerService.findById(id);
    		model.addAttribute("lawyer", lawyer);
    		
    		if(result.hasErrors()) {
    			return "Admin/editLawyer";
    		}
    		
    		if(!lawyerDto.getImageFile().isEmpty()) {
    			//delete old image
    			String uploadDir = "public/images/";
    			Path oldImagePath = Paths.get(uploadDir + lawyer.getImageFileName());
    			
    			try {
    				Files.delete(oldImagePath);
    			}catch(Exception e) {
    				System.out.println("Exception: "+e.getMessage());
    			}
    			
    			//save the new image file
    			MultipartFile image=lawyerDto.getImageFile();
    	    	String StorageFileName="Lawyer_"+image.getOriginalFilename();
    	    	
    	    	try(InputStream inputStream=image.getInputStream()){
        			Files.copy(inputStream, Paths.get(uploadDir + StorageFileName),
        					StandardCopyOption.REPLACE_EXISTING);
        		}
    	    	
    	    	lawyer.setImageFileName(StorageFileName);
    		}
    		
    		lawyer.setFirstName(lawyerDto.getFirstName());
    		lawyer.setLastName(lawyerDto.getLastName());
        	lawyer.setEmail(lawyerDto.getEmail());
        	lawyer.setPhone(lawyerDto.getPhone());
        	lawyer.setSpecialization(lawyerDto.getSpecialization());
        	
        	//save the updated lawyer details
        	lawyerService.save(lawyer);
    		
    	}catch(Exception ex) {
    		System.out.println("Exception: "+ex.getMessage());
    	}
    	
    	return "redirect:/lawyer/profile";
    }
    //profile section ends
}

