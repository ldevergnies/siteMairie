package com.filrouge.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.UtilisateurDao;

public class ConnexionForm {
	private UtilisateurDao utilisateurDao;
	
	public ConnexionForm( UtilisateurDao utilisateurDao ) {
	    this.utilisateurDao = utilisateurDao;
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
	
	public boolean validerFormulaire(HttpServletRequest request){
		String adresseEmail = getValeurChamp(request, "adresseEmail");
		String password = getValeurChamp(request, "password");
		
		validationAdresseEmail(adresseEmail);
		validationPassword(password);
		validationConnexion(adresseEmail, password);
		
		boolean estValide = erreurs.isEmpty() ? true : false;
		
		if (estValide) {
			resultat = "Connexion effectuée";
		} else {
			resultat = "Echec de la connexion";
		}
		
		return estValide;
	}
	
	private void validationAdresseEmail (String email) {
		if (email == null) {
			setErreur("adresseEmail", "Merci de saisir votre adresse email");
		} else if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")){
			setErreur("adresseEmail", "Merci de saisir une adresse email valide");
		}
	}
	
	private void validationPassword (String password) {
		if (password == null) {
			setErreur("password", "Merci de saisir votre mot de passe");
		}
	}
	
	private void validationConnexion (String email, String password) {
		String password2="";
		
			Utilisateur tmp = null;
			tmp = utilisateurDao.trouver(email);
			if (tmp != null){
				password2 = tmp.getPassword();
			}
		
		if (!password2.equals(password)){
			setErreur("password", "Mot de passe incorrecte");
		}
	}
}
