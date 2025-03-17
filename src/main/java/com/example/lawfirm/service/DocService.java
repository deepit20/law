package com.example.lawfirm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lawfirm.model.Case;
import com.example.lawfirm.model.Doc;
import com.example.lawfirm.repository.DocRepository;

@Service
public class DocService {
	private final DocRepository docRepository;
    public DocService(DocRepository docRepository) {
		this.docRepository = docRepository; 
	}
    public List<Doc> getAllDocs() {
		return docRepository.findAll();
	}
	public void save(Doc doc) {
		// TODO Auto-generated method stub
		docRepository.save(doc);
	}
	public Doc getDocumentById(Long docId) {
		// TODO Auto-generated method stub
		return docRepository.findById(docId).get();
	}
	public List<Doc> findByCases(Case caseDetails) {
		// TODO Auto-generated method stub
		return docRepository.findByCases(caseDetails);
	}
	public void delete(Doc doc) {
		// TODO Auto-generated method stub
		docRepository.delete(doc);
	}
}
