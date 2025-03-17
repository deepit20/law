package com.example.lawfirm.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Lawyer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate DOB=null;
	private String email;
	private String specialization;
	private String phone;
	private String password;
	private String imageFileName;
	
	@OneToMany(mappedBy = "lawyer")
    private List<Case> cases;
	
	
	public LocalDate getDOB() {
		return DOB;
	}
	public void setDOB(LocalDate dOB) {
		DOB = dOB;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String image_file_name) {
		this.imageFileName = image_file_name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String name) {
		this.firstName = name;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long lawyer_id) {
		this.id = lawyer_id;
	}
	public List<Case> getCases() {
		return cases;
	}
	public void setCases(List<Case> cases) {
		this.cases = cases;
	}
}
