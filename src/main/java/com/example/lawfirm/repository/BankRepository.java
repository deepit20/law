package com.example.lawfirm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lawfirm.model.Bank;

public interface BankRepository extends JpaRepository<Bank,Long> {

}
