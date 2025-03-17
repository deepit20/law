package com.example.lawfirm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lawfirm.model.Case;
import com.example.lawfirm.model.Doc;

public interface DocRepository extends JpaRepository<Doc,Long> {
	List<Doc> findByCases(Case cases);
}
