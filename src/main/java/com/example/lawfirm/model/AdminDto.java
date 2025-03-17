package com.example.lawfirm.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotEmpty;

public class AdminDto {
    @NotEmpty(message = "The firstName is required")
    private String firstName;
    
    @NotEmpty(message = "The lastName is required")
    private String lastName;
    
    private LocalDate DOB=null;

    @NotEmpty(message = "The email is required")
    private String email;

    @NotEmpty(message = "The specialization is required")
    private String specialization;

    @NotEmpty(message = "Phone is required")
    private String phone;

    private MultipartFile imageFile;

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public LocalDate getDOB() {
		return DOB;
	}

	public void setDOB(LocalDate dOB) {
		DOB = dOB;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}