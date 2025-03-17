package com.example.lawfirm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lawfirm.model.Case;

public interface CaseRepository extends JpaRepository<Case,Long> {

}
