package com.example.lawfirm.model;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class DocDto {
	private String cases;
	
	private MultipartFile docFile;

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public MultipartFile getDocFile() {
		return docFile;
	}

	public void setDocFile(MultipartFile docFile) {
		this.docFile = docFile;
	}
}
