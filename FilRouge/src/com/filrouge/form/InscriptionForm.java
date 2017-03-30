package com.filrouge.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.UtilisateurDao;

public class InscriptionForm {
	private Map<String, String> erreurs = new HashMap<String, String> ();
	private String resultat;
	
	
	public Map<String, String> getErreurs () {
		return erreurs;
	}
	
	private void setErreur( String champ, String message ) {
	    erreurs.put( champ, message );
	}
	
	public String getResultat () {
		return resultat;
	}
	
	private String getValeurChamp( HttpServletRequest request, String nomChamp ) {
	    String valeur = request.getParameter( nomChamp );
	    if ( valeur == null || valeur.trim().length() == 0 ) {
	        return null;
	    } else {
	        return valeur.trim();
	    }
	}
	
	public boolean validerFormulaire (HttpServletRequest request, boolean estAdmin, UtilisateurDao utilisateurDao) {
		String nom = this.getValeurChamp(request, "nom");
		String prenom = this.getValeurChamp(request, "prenom");
		String adresse = this.getValeurChamp(request, "adresse");
		String codePostal = this.getValeurChamp(request, "codePostal");
		String ville = this.getValeurChamp(request, "ville");
		String adresseEmail = this.getValeurChamp(request, "adresseEmail");
		String numeroTelephone = this.getValeurChamp(request, "telephone");
		String password = this.getValeurChamp(request, "password");
		String password2 = this.getValeurChamp(request, "password2");
		
		validationNom(nom);
		validationPrenom(prenom);
		validationAdresse(adresse);
		validationCodePostal(codePostal);
		validationVille(ville);
		validationEmail(adresseEmail, utilisateurDao);
		validationTelephone(numeroTelephone);
		validationPassword(password, password2);
		
		if (estAdmin){
			String accessLevel = this.getValeurChamp(request, "accessLevel");
			validationAccess(accessLevel);
		}
		
		boolean estValide = erreurs.isEmpty() ? true : false;
		
		if (estValide) {
			resultat = "Inscription effectuée";
		} else {
			resultat = "Formulaire invalide";
		}
		
		return estValide;
	}
	
	private void validationNom (String nom) {
		if (nom == null) {
			setErreur("nom", "Merci de saisir votre nom");
		} else if (nom.length() < 2) {
			setErreur("nom", "Merci de saisir votre nom complet");
		} else if (nom.length() > 25) {
			setErreur("nom", "Ne pas dépasser 25 caractères");
		} else if (!nom.matches("[a-zA-Z ]+")){
			setErreur("nom", "Merci de n'utiliser que des lettres");
		}
	}
	private void validationAccess (String access) {
		if (!access.equals("admin") && !access.equals("cantine") && !access.equals("garderie") && !access.equals("activite") && !access.equals("parent")) {
			setErreur("access", "Niveau d'accréditation incorrecte");
		}
	}
	
	private void validationPrenom (String prenom) {
		if (prenom == null) {
			setErreur("prenom", "Merci de saisir votre prenom");
		} else if (prenom.length() < 2) {
			setErreur("prenom", "Merci de saisir votre prenom complet");
		} else if (prenom.length() > 25) {
			setErreur("prenom", "Ne pas dépasser 25 caractères");
		} else if (!prenom.matches("[a-zA-Z ]+")){
			setErreur("prenom", "Merci de n'utiliser que des lettres");
		}
	}
	
	private void validationAdresse (String adresse) {
		if (adresse == null) {
			setErreur("adresse", "Merci de saisir votre adresse");
		} else if (adresse.length() < 3) {
			setErreur("adresse", "Merci de saisir votre adresse complète");
		} else if (adresse.length() > 25) {
			setErreur("adresse", "Ne pas dépasser 25 caractères");
		} else if (!adresse.matches("[a-zA-Z0-9 ]+")){
			setErreur("adresse", "Merci de n'utiliser que des lettres et des chiffres");
		}
	}
	
	private void validationCodePostal (String codePostal) {
		if (codePostal == null){
			setErreur("codePostal", "Merci de saisir votre code postal");
		} else if (!codePostal.matches("[0-9]{2}+[ ]?+[0-9]{3}+")){
			setErreur("codePostal", "Merci de saisir un code postal valide");
		}
	}
	
	private void validationVille (String ville) {
		if (ville == null) {
			setErreur("ville", "Merci de saisir votre ville");
		} else if (ville.length() < 3) {
			setErreur("ville", "Merci de saisir votre ville complète");
		} else if (ville.length() > 25) {
			setErreur("ville", "Ne pas dépasser 25 caractères");
		} else if (!ville.matches("[a-zA-Z -/]+")){
			setErreur("ville", "Merci de n'utiliser que des lettres");
		}
	}
	
	private void validationEmail (String email, UtilisateurDao utilisateurDao) {
		
		if (email == null) {
			setErreur("adresseEmail", "Merci de saisir votre adresse email");
		} else if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")){
			setErreur("adresseEmail", "Merci de saisir une adresse email valide");
		} else {
			Utilisateur user = null;
			user = utilisateurDao.trouver(email);
			
			if (user != null){
				setErreur("adresseEmail", "Votre adresse email est déjà inscrite. Dommage !");
			}
		}
	}
	
	private void validationTelephone (String numeroTelephone) {
		if ((numeroTelephone != null) && !numeroTelephone.matches("[0-9]{2}+[ .]?+[0-9]{2}+[ .]?+[0-9]{2}+[ .]?+[0-9]{2}+[ .]?+[0-9]{2}+")){
			setErreur("telephone", "Merci d'utiliser le format 00 00 00 00 00");
		}
	}
	
	private void validationPassword (String password, String password2) {
		if (password == null) {
			setErreur("password", "Merci de saisir votre mot de passe");
		} else if (password.length() < 8){
			setErreur("password", "Le mot de passe doit contenir au minimum 8 caractères");
		} else if (password.length() > 25) {
			setErreur("password", "Ne pas dépasser 25 caractères");
		} else if (!password.equals(password2)){
			setErreur("password2", "Les mots de passes ne correspondent pas");
		}
	}
}
