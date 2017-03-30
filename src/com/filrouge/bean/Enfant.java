package com.filrouge.bean;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.*;

public class Enfant {
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setAnniversaire(LocalDate anniversaire) {
		this.anniversaire = anniversaire;
	}

	private String id;
	private String nom;
	private String prenom;
	private LocalDate anniversaire;
	private String parent1;
	private String parent2;
	
	public Enfant() {
		this("Christ", "Jesus", 25, 12, 0);
	}
	
	public Enfant (HttpServletRequest request, Utilisateur parent) {
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String date = request.getParameter("date");
		
		this.nom = nom;
		this.prenom = prenom;
		this.anniversaire = stringToDate(date);
		this.parent1 = parent.getId();
	}
	
	public Enfant (HttpServletRequest request, Utilisateur parent, String parent1) {
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String date = request.getParameter("date");
		
		this.nom = nom;
		this.prenom = prenom;
		this.anniversaire = stringToDate(date);
		this.parent1 = parent1;
		this.parent2 = parent.getId();
	}
	
	public Enfant (String nom, String prenom, int jourNaissance, int moisNaissance, int anneeNaissance){
		this.nom = nom;
		this.prenom = prenom;
		anniversaire = new LocalDate(anneeNaissance, moisNaissance, jourNaissance);
	}
	
	private LocalDate stringToDate(String date){
		int jour = Integer.parseInt(date.substring(0, 2));
		int mois = Integer.parseInt(date.substring(3, 5));
		int annee = Integer.parseInt(date.substring(6,10));
		LocalDate anniversaire = new LocalDate(annee, mois, jour);
		return anniversaire;
	}
	
	public String getNom () {
		return nom;
	}
	
	public String getPrenom () {
		return prenom;
	}
	
	public int getAge () {
		LocalDate now = new LocalDate();
		Years age = Years.yearsBetween(anniversaire, now);
		return age.getYears();
	}
	
	public LocalDate getAnniversaire () {
		return anniversaire;
	}

	public String getParent1() {
		return parent1;
	}

	public void setParent1(String parent1) {
		this.parent1 = parent1;
	}

	public String getParent2() {
		return parent2;
	}

	public void setParent2(String parent2) {
		this.parent2 = parent2;
	}
	
}
