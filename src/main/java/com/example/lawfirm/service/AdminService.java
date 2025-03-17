package com.example.lawfirm.service;

import org.springframework.stereotype.Service;
import com.example.lawfirm.repository.*;
import java.util.List;
import com.example.lawfirm.model.*;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) { 
        this.adminRepository = adminRepository; 
    }
    public List<Admin> getAllAdmins() { 
    	return adminRepository.findAll(); 
    }
	public void save(Admin admin) {
		// TODO Auto-generated method stub
		adminRepository.save(admin);
	}
	public Admin findById(long id) {
		// TODO Auto-generated method stub
		return adminRepository.findById(id).get();
	}
	public void delete(Admin admin) {
		// TODO Auto-generated method stub
		adminRepository.delete(admin);
	}
}