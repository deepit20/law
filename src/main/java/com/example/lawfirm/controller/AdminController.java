package com.example.lawfirm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.lawfirm.service.*;

import jakarta.validation.Valid;

import com.example.lawfirm.model.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class AdminController {
	@Autowired
    private final AdminService adminService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private CaseService caseService;
	@Autowired
	private LawyerService lawyerService;
	@Autowired
	private BankService bankService;
	@Autowired
	private DocService docService;
	
	
    public AdminController(AdminService adminService,ClientService cls,CaseService cs) { 
    	this.adminService = adminService; 
    	this.clientService=cls;
    	this.caseService=cs;
    }
    
    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
//        model.addAttribute("loyalClientsCount", clientService.count());
//        model.addAttribute("casesSolvedCount", caseService.count());
        model.addAttribute("meetingsCount", 50); // Placeholder for actual meetings count
        model.addAttribute("documentationsCount", 30); // Placeholder for actual documentations count
        model.addAttribute("courtAppearancesCount", 20); // Placeholder for actual court appearances count
        return "Admin/admin_dashboard";
    }

    //admin list page starts
    @GetMapping("/admin/admins")
    public String showAdmins(Model model) {
    	List<Admin> admin= adminService.getAllAdmins();
    	model.addAttribute("admin",admin);
    	return "Admin/admin";
    }
    
    @GetMapping("/addAdmin")
    public String showAddAdminPage(Model model) {
    	AdminDto adminDto=new AdminDto();
    	model.addAttribute("adminDto", adminDto);
    	return "Admin/addAdmin";
    }
    
    @PostMapping("/addAdmin")
    public String addAdmin(@Valid @ModelAttribute AdminDto adminDto,BindingResult result) {
    	if(adminDto.getImageFile().isEmpty()) {
    		result.addError(new FieldError("adminDto","imageFile","The image file is required"));
    	}
    	
    	if(adminDto.getDOB()==null) {
    		result.addError(new FieldError("lawyerDto","DOB","The date of birth is required"));
    	}
    	
    	if(result.hasErrors()) {
    		return "Admin/addAdmin";
    	}
    	
    	//save image file
    	MultipartFile image=adminDto.getImageFile();
    	String StorageFileName="Admin_"+image.getOriginalFilename();
    	
    	try {
    		String uploadDir="public/images/";
    		Path uploadPath=Paths.get(uploadDir);
    		
    		if(!Files.exists(uploadPath)) {
    			Files.createDirectories(uploadPath);
    		}
    		
    		try(InputStream inputStream=image.getInputStream()){
    			Files.copy(inputStream, Paths.get(uploadDir + StorageFileName),
    					StandardCopyOption.REPLACE_EXISTING);
    		}
    	}catch(Exception ex) {
    		System.out.println("Exception: " + ex.getMessage());
    	}
    	
    	//format date
        LocalDate date = adminDto.getDOB();
        
        // Convert LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(formatter);
    	
        
    	Admin admin = new Admin();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
    	admin.setDOB(adminDto.getDOB());
    	admin.setEmail(adminDto.getEmail());
    	admin.setPhone(adminDto.getPhone());
    	admin.setSpecialization(adminDto.getSpecialization());
    	admin.setImageFileName(StorageFileName);
    	admin.setPassword(adminDto.getFirstName()+"@"+formattedDate);
    	
    	adminService.save(admin);
    	
    	return "redirect:/admin/admins";
    }
    
    @GetMapping("/admins/edit")
    public String showAdminEditPage(Model model,@RequestParam Long id) {
    	
    	try {
    		Admin admin = adminService.findById(id);
    		model.addAttribute("admin", admin);
    		
    		AdminDto adminDto = new AdminDto();
    	    adminDto.setFirstName(admin.getFirstName());
        	adminDto.setLastName(admin.getLastName());
        	adminDto.setDOB(admin.getDOB());
        	adminDto.setEmail(admin.getEmail());
        	adminDto.setPhone(admin.getPhone());
        	adminDto.setSpecialization(admin.getSpecialization());
        	
        	model.addAttribute("adminDto", adminDto);
    	}catch(Exception ex) {
    		System.out.println("Exception:"+ex.getMessage());
    		return "redirect:/admin/admins";
    	}
    	return "Admin/editAdmin";
    }
    
    @PostMapping("/admins/edit")
    public String updateAdmin(Model model,@RequestParam long id,@Valid @ModelAttribute AdminDto adminDto,BindingResult result){
    	
    	try {
    		Admin admin = adminService.findById(id);
    		model.addAttribute("admin", admin);
    		
    		if(result.hasErrors()) {
    			return "Admin/editAdmin";
    		}
    		
    		if(!adminDto.getImageFile().isEmpty()) {
    			//delete old image
    			String uploadDir = "public/images/";
    			Path oldImagePath = Paths.get(uploadDir + admin.getImageFileName());
    			
    			try {
    				Files.delete(oldImagePath);
    			}catch(Exception e) {
    				System.out.println("Exception: "+e.getMessage());
    			}
    			
    			//save the new image file
    			MultipartFile image=adminDto.getImageFile();
    	    	String StorageFileName="Lawyer_"+image.getOriginalFilename();
    	    	
    	    	try(InputStream inputStream=image.getInputStream()){
        			Files.copy(inputStream, Paths.get(uploadDir + StorageFileName),
        					StandardCopyOption.REPLACE_EXISTING);
        		}
    	    	
    	    	admin.setImageFileName(StorageFileName);
    		}
    		
    		admin.setFirstName(adminDto.getFirstName());
    		admin.setLastName(adminDto.getLastName());
        	admin.setEmail(adminDto.getEmail());
        	admin.setPhone(adminDto.getPhone());
        	admin.setSpecialization(adminDto.getSpecialization());
        	
        	//save the updated lawyer details
        	adminService.save(admin);
    		
    	}catch(Exception ex) {
    		System.out.println("Exception: "+ex.getMessage());
    	}
    	
    	return "redirect:/admin/admins";
    }
    
    @GetMapping("/admins/delete")
    public String deleteAdmin(@RequestParam long id) {
    	
    	try {
    		Admin admin = adminService.findById(id);
    		
    		
    		//delete the image
    		Path imagePath=Paths.get("public/images/"+admin.getImageFileName());
    		
    		try {
    			Files.delete(imagePath);
    		}catch(Exception e) {
        		System.out.println("Exception: "+e.getMessage());
        	}
    		
    		//delete the lawyer
    		adminService.delete(admin);
    	}catch(Exception e) {
    		System.out.println("Exception: "+e.getMessage());
    	}
    	return "redirect:/admin/admins";
    }
    //admin list page ends
    
    //our associates page starts
    @GetMapping("/admin/associates")
    public String showAssociatesPage(Model model) {
    	List<Lawyer> lawyer= lawyerService.getAllLawyers();
    	model.addAttribute("lawyers",lawyer);
    	return "Admin/associate";
    }
    
    @GetMapping("/addLawyer")
    public String showAddLawyerPage(Model model) {
    	LawyerDto lawyerDto=new LawyerDto();
    	model.addAttribute("lawyerDto", lawyerDto);
    	return "Admin/addLawyer";
    }
    
    @PostMapping("/addLawyer")
    public String addLawyer(@Valid @ModelAttribute LawyerDto lawyerDto,BindingResult result) {
    	if(lawyerDto.getImageFile().isEmpty()) {
    		result.addError(new FieldError("lawyerDto","imageFile","The image file is required"));
    	}
    	
    	if(lawyerDto.getDOB()==null) {
    		result.addError(new FieldError("lawyerDto","DOB","The date of birth is required"));
    	}
    	
    	if(result.hasErrors()) {
    		return "Admin/addLawyer";
    	}
    	
    	//save image file
    	MultipartFile image=lawyerDto.getImageFile();
    	String StorageFileName="Lawyer_"+image.getOriginalFilename();
    	
    	try {
    		String uploadDir="public/images/";
    		Path uploadPath=Paths.get(uploadDir);
    		
    		if(!Files.exists(uploadPath)) {
    			Files.createDirectories(uploadPath);
    		}
    		
    		try(InputStream inputStream=image.getInputStream()){
    			Files.copy(inputStream, Paths.get(uploadDir + StorageFileName),
    					StandardCopyOption.REPLACE_EXISTING);
    		}
    	}catch(Exception ex) {
    		System.out.println("Exception: " + ex.getMessage());
    	}
    	
    	//format date
        LocalDate date = lawyerDto.getDOB();
        
        // Convert LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(formatter);
    	
        
    	Lawyer lawyer=new Lawyer();
    	lawyer.setFirstName(lawyerDto.getFirstName());
    	lawyer.setLastName(lawyerDto.getLastName());
    	lawyer.setDOB(lawyerDto.getDOB());
    	lawyer.setEmail(lawyerDto.getEmail());
    	lawyer.setPhone(lawyerDto.getPhone());
    	lawyer.setSpecialization(lawyerDto.getSpecialization());
    	lawyer.setImageFileName(StorageFileName);
    	lawyer.setPassword(lawyerDto.getFirstName()+"@"+formattedDate);
    	
    	lawyerService.save(lawyer);
    	
    	return "redirect:/admin/associates";
    }
    
    @GetMapping("/associates/edit")
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
    		return "redirect:/admin/associates";
    	}
    	return "Admin/editLawyer";
    }
    
    @PostMapping("/associates/edit")
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
    	
    	return "redirect:/admin/associates";
    }
    
    @GetMapping("/associates/delete")
    public String deleteLawyer(@RequestParam long id) {
    	
    	try {
    		Lawyer lawyer = lawyerService.findById(id);
    		
    		
    		//delete the image
    		Path imagePath=Paths.get("public/images/"+lawyer.getImageFileName());
    		
    		try {
    			Files.delete(imagePath);
    		}catch(Exception e) {
        		System.out.println("Exception: "+e.getMessage());
        	}
    		
    		//delete the lawyer
    		lawyerService.delete(lawyer);
    	}catch(Exception e) {
    		System.out.println("Exception: "+e.getMessage());
    	}
    	return "redirect:/admin/associates";
    }
    //our associates page ends
    
    //our client page starts
    @GetMapping("/admin/clients")
    public String showClientPage(Model model) {
    	List<Client> client= clientService.getAllClients();
    	model.addAttribute("client",client);
    	return "Admin/client";
    }
    
    @GetMapping("/addClient")
    public String showAddClientPage(Model model) {
    	ClientDto clientDto=new ClientDto();
    	model.addAttribute("clientDto", clientDto);
    	model.addAttribute("bank", bankService.getAllBanks());
    	return "Admin/addClient";
    }
    
    @PostMapping("/addClient")
    public String addClient(@Valid @ModelAttribute ClientDto clientDto,BindingResult result) {
    	if(result.hasErrors()) {
    		return "Admin/addClient";
    	}
    	
        String bankIdStr = clientDto.getBank();
        long bankId = Long.parseLong(bankIdStr);
        System.out.println(bankId);  
        
        Bank bank =bankService.findById(bankId);
    	
    	Client client = new Client();
    	client.setBank(bank);
    	client.setBranchName(clientDto.getBranchName());
    	client.setEmail(clientDto.getEmail());
    	client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
    	client.setPassword(clientDto.getBranchName()+"@123");
    	
    	clientService.save(client);
    	
    	return "redirect:/admin/clients";
    }
    
    @GetMapping("/clients/edit")
    public String showClientEditPage(Model model,@RequestParam Long id) {
    	
    	try {
    		Client client = clientService.findById(id);
    		model.addAttribute("client", client);	
    		model.addAttribute("bank", bankService.getAllBanks());
    		
    		String bankIdStr = String.valueOf(client.getBank().getId());
    		
    		ClientDto clientDto = new ClientDto();
    		clientDto.setBank(bankIdStr);
        	clientDto.setBranchName(client.getBranchName());
        	clientDto.setEmail(client.getEmail());
        	clientDto.setPhone(client.getPhone());
        	clientDto.setAddress(client.getAddress());
        	
        	model.addAttribute("clientDto", clientDto);
    	}catch(Exception ex) {
    		System.out.println("Exception:"+ex.getMessage());
    		return "redirect:/admin/associates";
    	}
    	return "Admin/editClient";
    }
    
    @PostMapping("/clients/edit")
    public String updateClient(Model model,@RequestParam long id,@Valid @ModelAttribute ClientDto clientDto,BindingResult result){
    	
    	try {
    		Client client = clientService.findById(id);
    		model.addAttribute("client", client);
    		
    		if(result.hasErrors()) {
    			return "Admin/editClient";
    		}
    		
    		String bankIdStr = clientDto.getBank();
            long bankId = Long.parseLong(bankIdStr);
            System.out.println(bankId);  
            
            Bank bank =bankService.findById(bankId);
    		
    		client.setBank(bank);
    		client.setBranchName(clientDto.getBranchName());
        	client.setEmail(clientDto.getEmail());
        	client.setPhone(clientDto.getPhone());
        	client.setAddress(clientDto.getAddress());
        	
        	//save the updated lawyer details
        	clientService.save(client);
    		
    	}catch(Exception ex) {
    		System.out.println("Exception: "+ex.getMessage());
    	}
    	
    	return "redirect:/admin/clients";
    }
    
    @GetMapping("/clients/delete")
    public String deleteClient(@RequestParam long id) {
    	
    	try {
    		Client client = clientService.findById(id);
    		
    		//delete the client
    		clientService.delete(client);
    	}catch(Exception e) {
    		System.out.println("Exception: "+e.getMessage());
    	}
    	return "redirect:/admin/clients";
    }
    
    //our client page ends
    
    //case page starts
    @GetMapping("/admin/cases")
    public String showCasePage(Model model) {
    	List<Case> cases = caseService.getAllCases();
    	model.addAttribute("cases",cases);
    	return "Admin/case";
    }
    
    @GetMapping("/addCases")
    public String showAddCasesPage(Model model) {
    	CaseDto caseDto=new CaseDto();
    	model.addAttribute("caseDto", caseDto);
    	List<Client> client= clientService.getAllClients();
    	model.addAttribute("client",client);
    	List<Lawyer> lawyer = lawyerService.getAllLawyers();
    	model.addAttribute("lawyer", lawyer);
    	List<Admin> admin = adminService.getAllAdmins();
    	model.addAttribute("admin",admin);
    	return "Admin/addCase";
    }
    
    @PostMapping("/addCases")
    public String addCase(@Valid @ModelAttribute CaseDto caseDto,BindingResult result,Model model) {
    	
    	// Check for validation errors
        if (result.hasErrors()) {
            return "Admin/addCase"; // Return to the form view if there are errors
        }

        // Get the present date
        LocalDate currentDate = LocalDate.now();

        // Generate a random case number
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000); // Random number between 10000 and 99999

        // Create the case number
        String caseNum = caseDto.getCaseType() + "_" + randomNumber;
        
        //foriegn key admin,lawyer,and client
        String adminIdStr = caseDto.getAdmin();
        long adminId = Long.parseLong(adminIdStr);
        System.out.println(adminId);  
        
        Admin admin =adminService.findById(adminId);
        
        String lawyerIdStr = caseDto.getLawyer();
        long lawyerId = Long.parseLong(lawyerIdStr);
        System.out.println(lawyerId);  
        
        Lawyer lawyer = lawyerService.findById(lawyerId);
        
        String clientIdStr = caseDto.getClient();
        long clientId = Long.parseLong(clientIdStr);
        System.out.println(clientId); 
        
        Client client = clientService.findById(clientId);
        
        
        // Create a new Case entity
        Case cases = new Case();
        cases.setCaseType(caseDto.getCaseType());
        cases.setDescription(caseDto.getDescription());
        cases.setStatus(caseDto.getStatus());
        cases.setCaseNumber(caseNum);
        cases.setDate(currentDate); 
        cases.setAdmin(admin);
        cases.setLawyer(lawyer);
        cases.setClient(client);

        // Save the case
        caseService.save(cases);

        // Redirect to the cases list page
        return "redirect:/admin/cases";
    	
    }
    
    @GetMapping("/cases/edit")
    public String showCasesEditPage(Model model,@RequestParam Long id) {   	
    	try {
    		Case cases = caseService.findById(id);
    		model.addAttribute("case", cases);
    		
    		String adminIdStr = String.valueOf(cases.getAdmin().getId());
    		
    		String clientIdStr = String.valueOf(cases.getClient().getId());
    		
    		String lawyerIdStr = String.valueOf(cases.getLawyer().getId());
    		
    		
    		CaseDto caseDto = new CaseDto();
    		caseDto.setAdmin(adminIdStr);
    		caseDto.setClient(clientIdStr);
    		caseDto.setLawyer(lawyerIdStr);
    		caseDto.setCaseType(cases.getCaseType());
    		caseDto.setDescription(cases.getDescription());
    		caseDto.setStatus(cases.getStatus());
        	
        	model.addAttribute("caseDto", caseDto);
        	
        	
        	List<Client> client= clientService.getAllClients();
        	model.addAttribute("client",client);
        	
        	List<Lawyer> lawyer = lawyerService.getAllLawyers();
        	model.addAttribute("lawyer", lawyer);
        	
        	List<Admin> admin = adminService.getAllAdmins();
        	model.addAttribute("admin",admin);
        	System.out.println("Admins="+admin);
    	}catch(Exception ex) {
    		System.out.println("Exception:"+ex.getMessage());
    		return "redirect:/admin/cases";
    	}
    	return "Admin/editCase";
    }
    
    @PostMapping("/cases/edit")
    public String updateCase(Model model, @RequestParam long id, @Valid @ModelAttribute CaseDto caseDto, BindingResult result) {
        try {
            Case cases = caseService.findById(id);
            model.addAttribute("case", cases);
            
            if (result.hasErrors()) {
                System.out.println("Validation errors: " + result.getAllErrors());
                return "Admin/editCase";
            }
            
            // Parse IDs and find entities
            long adminId = Long.parseLong(caseDto.getAdmin());
            Admin admin = adminService.findById(adminId);
            
            long lawyerId = Long.parseLong(caseDto.getLawyer());
            Lawyer lawyer = lawyerService.findById(lawyerId);
            
            long clientId = Long.parseLong(caseDto.getClient());
            Client client = clientService.findById(clientId);
            
            // Update case details
            cases.setDescription(caseDto.getDescription());
            cases.setStatus(caseDto.getStatus());
            cases.setAdmin(admin);
            cases.setLawyer(lawyer);
            cases.setClient(client);
            cases.setCaseType(caseDto.getCaseType());
            
            // Save the updated case details
            caseService.save(cases);
            
            System.out.println("Case updated successfully.");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        
        return "redirect:/admin/cases";
    }
    @GetMapping("/cases/delete")
    public String deleteCase(@RequestParam long id) {
    	
    	try {
    		Case cases = caseService.findById(id);
    		
    		//delete the lawyer
    		caseService.delete(cases);
    	}catch(Exception e) {
    		System.out.println("Exception: "+e.getMessage());
    	}
    	return "redirect:/admin/cases";
    }
    
    @GetMapping("/admin/cases/doc")
    public String viewDoc(Model model,@RequestParam Long id) {
    	try {
    		Optional<Case> caseOptional = caseService.getById(id);
            if (caseOptional.isPresent()) {
                Case caseDetails = caseOptional.get();
                List<Doc> caseDocuments = docService.findByCases(caseDetails); // Fetch only related docs
                model.addAttribute("caseDetails", caseDetails);
                model.addAttribute("doc", caseDocuments);
                return "Admin/viewDoc";
            }
    	}catch(Exception e) {
    		System.out.println("Exception: "+e.getMessage());
    	}
    	return "redirect:/admin/cases";
    }
    
    @GetMapping("/admin/case/document/download/{docId}")
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
    //case page ends
    
    //bank page starts
    
    @GetMapping("/admin/banks")
    public String showBankPage(Model model) {
    	List<Bank> bank=bankService.getAllBanks();
    	model.addAttribute("bank", bank);
    	return "Admin/bankList";
    }
    
    @GetMapping("/addBank")
    public String showAddBankPage(Model model) {
    	model.addAttribute("bank", new Bank());
    	return "Admin/addBank";
    }
    
    @PostMapping("/addBank")
    public String addBank(@Valid @ModelAttribute Bank bank,BindingResult result,Model model) {
        Bank newBank = new Bank();
        
        newBank.setName(bank.getName());

        // Save the bank
        bankService.save(newBank);

        // Redirect to the cases list page
        return "redirect:/admin/banks";
    	
    }
    
    @GetMapping("/bank/edit")
    public String showBankEditPage(Model model,@RequestParam Long id) { 
    	Bank bank = bankService.findById(id);
    	model.addAttribute("bank", bank);
    	return "Admin/editBank";
    }
    
    @PostMapping("/bank/edit")
    public String updateBank(Model model, @RequestParam long id, @Valid @ModelAttribute Bank bank, BindingResult result) {
    	try {
            Bank bank1 = bankService.findById(id);
            
            bank1.setName(bank.getName());
            
            // Save the updated case details
            bankService.save(bank1);
            
            System.out.println("Case updated successfully.");
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        
        return "redirect:/admin/banks";
    }
    
    @GetMapping("/bank/delete")
    public String deleteBank(@RequestParam long id) {
    	
    	try {
    		Bank bank = bankService.findById(id);
    		
    		//delete the lawyer
    		bankService.delete(bank);
    	}catch(Exception e) {
    		System.out.println("Exception: "+e.getMessage());
    	}
    	return "redirect:/admin/banks";
    }
    
    //bank page ends
}
