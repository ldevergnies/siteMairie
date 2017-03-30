package com.filrouge.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.UtilisateurDao;

public class InscriptionEnfantForm {
private EnfantDao enfantDao;
	
	public InscriptionEnfantForm( EnfantDao enfantDao ) {
	    this.enfantDao = enfantDao;
	}
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
	
	public String creerEnfant (HttpServletRequest request, Utilisateur parent, HttpSession session) {
		String nom = getValeurChamp(request, "nom");
		String prenom = getValeurChamp(request, "prenom");
		String resultat = "???";
		
		Enfant enfant = enfantDao.trouver(nom, prenom);
		if(enfant != null){
			if(enfant.getParent2()!= null && enfant.getParent2().length() != 0){
				resultat = "L'enfant a déjà deux parents.";
			} else {
				if ( enfant.getParent1().equals(parent.getId())){
					resultat = "Vous l'avez déjà inscrit.";
				} else {
					enfant.setParent2(parent.getId());
					enfantDao.modifier(enfant);
					session.setAttribute("enfant", enfant);
					resultat = "Vous avez été attribué à l'enfant";
				}
			}
		} else {
			enfant = new Enfant (request, parent);
			session.setAttribute("enfant", enfant);
			resultat = "L'enfant a été inscrit";
			enfantDao.creer(enfant);
		}
		
		return resultat;
	}
	
	public boolean validerFormulaire (HttpServletRequest request, boolean estAdmin, UtilisateurDao utilisateurDao) {
		String nom = getValeurChamp(request, "nom");
		String prenom = getValeurChamp(request, "prenom");
		String date = getValeurChamp(request, "date");
		
		validationNom(nom);
		validationPrenom(prenom);
		validationDate(date);
		
		if (estAdmin){
			String email = this.getValeurChamp(request, "email");
			validationEmail(email, utilisateurDao);
		}
		
		boolean estValide = erreurs.isEmpty() ? true : false;
		
		if (estValide) {
			resultat = "Inscription effectuée";
		} else {
			resultat = "Formulaire invalide";
		}
		
		return estValide;
	}
	
private void validationEmail (String email, UtilisateurDao utilisateurDao) {
		
		if (email == null) {
			setErreur("email", "Merci de saisir votre adresse email");
		} else if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")){
			setErreur("email", "Merci de saisir une adresse email valide");
		} else {
			Utilisateur user = null;
			user = utilisateurDao.trouver(email);
			
			if (user == null){
				setErreur("email", "Vous n'existez pas");
			}
		}
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
	
	private void validationDate (String date) {
		if (date == null) {
			setErreur("date", "Merci de saisir la date de naissance");
		} else if (!date.matches("[0-9]{2}+[/]{1}+[0-9]{2}+[/]{1}+[0-9]{4}+")){
			setErreur("date", "Merci d'utiliser le format JJ/MM/AAAA");
		}
	}
}
