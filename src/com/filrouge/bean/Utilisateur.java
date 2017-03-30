package com.filrouge.bean;

import javax.servlet.http.HttpServletRequest;

public class Utilisateur {
	private String id;
	public void setId(String id) {
		this.id = id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	private String nom;
	private String prenom;
	private String adresse;
	private String codePostal;
	private String ville;
	private String adresseEmail;
	private String numeroTelephone;
	private String password;
	private String access = "parent";
	
	public Utilisateur () {
		this("Le Père", "Dieu", "avenue du Temple", "00000", "Jérusalem Céleste", "dieu.lepere@trinite.ciel", "00 00 00 00 00", "Esprit-Saint");
	}
	
	public Utilisateur (String nom, String prenom, String adresse, String codePostal, String ville, String adresseEmail, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.codePostal = codePostal;
		this.ville = ville;
		this.adresseEmail = adresseEmail;
		this.password = password;
	}
	
	public Utilisateur (String nom, String prenom, String adresse, String codePostal, String ville, String adresseEmail, String numeroTelephone, String password) {
		this(nom, prenom, adresse, codePostal, ville, adresseEmail, password);
		this.numeroTelephone = numeroTelephone;
		this.numeroTelephone = formatTelephone(this.numeroTelephone);
	}
	
	public Utilisateur (String id, String nom, String prenom, String adresse, String codePostal, String ville, String adresseEmail, String numeroTelephone, String password) {
		this(nom, prenom, adresse, codePostal, ville, adresseEmail, numeroTelephone, password);
		this.id = id;
	}
	
	public Utilisateur (HttpServletRequest request, boolean estAdmin) {
		this.nom = request.getParameter("nom");
		this.prenom = request.getParameter("prenom");
		this.adresse = request.getParameter("adresse");
		this.codePostal = request.getParameter("codePostal");
		this.ville = request.getParameter("ville");
		this.adresseEmail = request.getParameter("adresseEmail");
		this.numeroTelephone = request.getParameter("telephone");
		this.numeroTelephone = formatTelephone(this.numeroTelephone);
		this.password = request.getParameter("password");
		if (estAdmin) {
			this.access = request.getParameter("accessLevel");
		}
		}
	
	public String getId () {
		return id;
	}
	
	public void setAccess (String access) {
		this.access = access;
	}
	
	public String getAccess () {
		return access;
	}
	
	public String getNom () {
		return nom;
	}
	
	public String getPrenom () {
		return prenom;
	}
	
	public void setAdresse (String adresse) {
		this.adresse = adresse;
	}
	
	public String getAdresse () {
		return adresse;
	}
	
	public void setCodePostal (String codePostal) {
		this.codePostal = codePostal;
	}
	
	public String getCodePostal () {
		return codePostal;
	}
	
	public void setVille (String ville) {
		this.ville = ville;
	}
	
	public String getVille () {
		return ville;
	}
	
	public void setAdresseEmail (String adresseEmail) {
		this.adresseEmail = adresseEmail;
	}
	
	public String getAdresseEmail () {
		return adresseEmail;
	}
	
	public void setNumeroTelephone (String numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}
	
	public String getNumeroTelephone () {
		return numeroTelephone;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public String getPassword () {
		return password;
	}
	
	private String formatTelephone (String telephone){
		if (telephone == null || telephone.trim().length() == 0) {
			return telephone;
		}
		if (telephone.matches("[0-9]{2}+[.]+[0-9]{2}+[.]+[0-9]{2}+[.]+[0-9]{2}+[.]+[0-9]{2}+")){
			telephone.replace('.', ' ');
		} else if (telephone.matches("[0-9]{2}+[0-9]{2}+[0-9]{2}+[0-9]{2}+[0-9]{2}+")){
			String telephoneTmp = "";
			for (int i = 0 ; i < 9; i+=2 ){
				telephoneTmp += telephone.substring(i, i+2);
				if (i<8){
					telephoneTmp += " ";
				}
			}
			telephone = telephoneTmp;
		}
		return telephone;
	}
}
