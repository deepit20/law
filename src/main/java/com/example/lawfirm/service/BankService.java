package com.example.lawfirm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lawfirm.model.*;
import com.example.lawfirm.repository.BankRepository;

@Service
public class BankService {
    private final BankRepository bankRepository;
    public BankService(BankRepository bankRepository) {
    	this.bankRepository = bankRepository; 
    }
    public List<Bank> getAllBanks() { 
    	return bankRepository.findAll();
    }
	public void save(Bank newBank) {
		// TODO Auto-generated method stub
		bankRepository.save(newBank);
	}
	public Bank findById(Long id) {
		// TODO Auto-generated method stub
		return bankRepository.findById(id).get();
	}
	public void delete(Bank bank) {
		// TODO Auto-generated method stub
		bankRepository.delete(bank);
	}
}
