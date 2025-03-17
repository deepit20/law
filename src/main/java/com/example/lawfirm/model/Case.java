package com.example.lawfirm.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "cases") // Use "cases" to avoid reserved keyword issues
public class Case {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_number", nullable = false)
    private String caseNumber;

    @Column(nullable = false)
    private String description;

    @Column(name = "case_type", nullable = false)
    private String caseType;
    
    private LocalDate date;

    @Column(nullable = false)
    private String status;

    // Foreign key referencing Lawyer
    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;

    // Foreign key referencing Admin
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // Foreign key referencing Client
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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

    public Lawyer getLawyer() {
        return lawyer;
    }

    public void setLawyer(Lawyer lawyer) {
        this.lawyer = lawyer;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}