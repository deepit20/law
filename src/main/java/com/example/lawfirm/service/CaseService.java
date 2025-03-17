package com.example.lawfirm.service;

import org.springframework.stereotype.Service;
import com.example.lawfirm.repository.*;
import java.util.List;
import java.util.Optional;

import com.example.lawfirm.model.*;

@Service
public class CaseService {
    private final CaseRepository caseRepository;
    public CaseService(CaseRepository caseRepository) {
		this.caseRepository = caseRepository; 
	}
    public List<Case> getAllCases() {
		return caseRepository.findAll();
	}
	public void save(Case cases) {
		// TODO Auto-generated method stub
		caseRepository.save(cases);
	}
	public Case findById(Long id) {
		// TODO Auto-generated method stub
		return caseRepository.findById(id).get();
	}
	public void delete(Case cases) {
		// TODO Auto-generated method stub
		caseRepository.delete(cases);
	}
	public Optional<Case> getById(Long id) {
		// TODO Auto-generated method stub
		return caseRepository.findById(id);
	}
}