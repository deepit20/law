package com.example.lawfirm.model;

import jakarta.validation.constraints.*;

public class CaseDto {
    
    @Size(min=10,message="the description should be atleast 10 characters")
    @Size(max=2000,message="the description cannot exceed 200 characters")
    private String description;
    
    @NotEmpty(message = "The case type is required")
    private String caseType;

    @NotEmpty(message = "The status is required")
    private String status;
    
    @NotEmpty(message = "The client is required")
    private String client;
    
    @NotEmpty(message = "The lawyer is required")
    private String lawyer;

    @NotEmpty(message = "The admin is required")
	private String admin;
    
    public String getLawyer() {
		return lawyer;
	}

	public void setLawyer(String lawyer) {
		this.lawyer = lawyer;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
}