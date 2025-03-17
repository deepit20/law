package com.example.lawfirm.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotEmpty;

public class ClientDto {
    @NotEmpty(message = "The bankName is required")
    private String bank;
    
    @NotEmpty(message = "The branchName is required")
    private String branchName;

    @NotEmpty(message = "The email is required")
    private String email;

    @NotEmpty(message = "The address is required")
    private String address;

    @NotEmpty(message = "Phone is required")
    private String phone;

	public String getBank() {
		return bank;
	}

	public void setBank(String bankName) {
		this.bank = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}