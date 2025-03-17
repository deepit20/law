package com.example.lawfirm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lawfirm.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

}
