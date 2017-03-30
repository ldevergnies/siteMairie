package com.filrouge.bean;

import org.joda.time.LocalDate;

public abstract class Ticket {
	private String id;
	private String acheteur;
	private String beneficiaire;
	private LocalDate dateAchat;
	private LocalDate dateUtilisation;
	private double prixAchat;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAcheteur() {
		return acheteur;
	}
	public void setAcheteur(String acheteur) {
		this.acheteur = acheteur;
	}
	public String getBeneficiaire() {
		return beneficiaire;
	}
	public void setBeneficiaire(String beneficiaire) {
		this.beneficiaire = beneficiaire;
	}
	public LocalDate getDateAchat() {
		return dateAchat;
	}
	public void setDateAchat(LocalDate dateAchat) {
		this.dateAchat = dateAchat;
	}
	public LocalDate getDateUtilisation() {
		return dateUtilisation;
	}
	public void setDateUtilisation(LocalDate dateUtilisation) {
		this.dateUtilisation = dateUtilisation;
	}
	public double getPrixAchat() {
		return prixAchat;
	}
	public void setPrixAchat(double prixAchat) {
		this.prixAchat = prixAchat;
	}
}
