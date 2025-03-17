package com.example.lawfirm.service;

import org.springframework.stereotype.Service;
import com.example.lawfirm.repository.*;
import java.util.List;
import com.example.lawfirm.model.*;

@Service
public class LawyerService {
    private final LawyerRepository lawyerRepository;
    public LawyerService(LawyerRepository lawyerRepository) { 
    	this.lawyerRepository = lawyerRepository; 
    }
    
    public List<Lawyer> getAllLawyers() { 
    	return lawyerRepository.findAll(); 
    }
    
	public void save(Lawyer lawyer) {
		// TODO Auto-generated method stub
		lawyerRepository.save(lawyer);
	}

	public Lawyer findById(Long id) {
		// TODO Auto-generated method stub
		return lawyerRepository.findById(id).get();
	}

	public void delete(Lawyer lawyer) {
		// TODO Auto-generated method stub
		lawyerRepository.delete(lawyer);
	}
}
