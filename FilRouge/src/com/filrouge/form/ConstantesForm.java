package com.filrouge.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.filrouge.bean.TicketActivite;
import com.filrouge.bean.TicketCantine;
import com.filrouge.bean.TicketGarderie;
import com.filrouge.dao.ConstantesDao;

public class ConstantesForm {
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
	public ConstantesForm(){
	}
	
	public boolean validerForm(HttpServletRequest request, ConstantesDao constantesDao){
		String cantine = getValeurChamp(request, "cantine");
		String garderie = getValeurChamp(request, "garderie");
		String foot = getValeurChamp(request, "foot");
		String couture = getValeurChamp(request, "couture");
		validation(cantine);
		validation(garderie);
		validation(foot);
		validation(couture);
		estNonNul(cantine, garderie, foot, couture);
		boolean estValide = erreurs.isEmpty() ? true : false;
		if (estValide) {
			if (cantine != null){
				cantine = cantine.replace(',', '.');
				TicketCantine.setPrixVente(Double.parseDouble(cantine));
				constantesDao.modifier(Double.parseDouble(cantine), "prix_cantine");
			}
			if (garderie != null){
				garderie = garderie.replace(',', '.');
				TicketGarderie.setPrixVente(Double.parseDouble(garderie));
				constantesDao.modifier(Double.parseDouble(garderie), "prix_garderie");
			}
			if (foot != null){
				foot = foot.replace(',', '.');
				TicketActivite.setPrixVenteFoot(Double.parseDouble(foot));
				constantesDao.modifier(Double.parseDouble(foot), "prix_foot");
			}
			if (couture != null){
				couture = couture.replace(',', '.');
				TicketActivite.setPrixVenteCouture(Double.parseDouble(couture));
				constantesDao.modifier(Double.parseDouble(couture), "prix_couture");
			}
			resultat = "Inscription effectuée";
		} else {
			resultat = "Formulaire invalide";
		}
		return estValide;
	}
	
	private void estNonNul(String...  str){
		boolean estNul = true;
		for (int i = 0; i < str.length; i++) {
			if (str[i] != null) {
				estNul = false;
			}
		}
		if (estNul) {
			setErreur("vide","Allez un effort, juste un petit prix à changer.");
		}
	}
	
	private void validation (String str) {
		if (str == null){
			setErreur("vide", "Merci de saisir votre prix");
		} else if (!str.matches("[0-9,.]+")){
			setErreur("vide", "Merci de saisir un prix valide");
		}
	}
	
}
