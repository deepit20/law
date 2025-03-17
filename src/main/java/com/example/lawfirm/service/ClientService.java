package com.example.lawfirm.service;

import org.springframework.stereotype.Service;
import com.example.lawfirm.repository.*;
import java.util.List;
import com.example.lawfirm.model.*;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
    	this.clientRepository = clientRepository; 
    }
    public List<Client> getAllClients() { 
    	return clientRepository.findAll();
    }
	public Client findById(Long id) {
		// TODO Auto-generated method stub
		return clientRepository.findById(id).get();
	}
	public void save(Client client) {
		// TODO Auto-generated method stub
		clientRepository.save(client);
	}
	public void delete(Client client) {
		// TODO Auto-generated method stub
		clientRepository.delete(client);
	}
}
