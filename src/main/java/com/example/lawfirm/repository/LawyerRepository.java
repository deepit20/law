package com.example.lawfirm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lawfirm.model.Lawyer;

public interface LawyerRepository extends JpaRepository<Lawyer,Long> {

}
