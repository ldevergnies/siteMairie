package com.filrouge.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.LocalDate;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.bean.TicketCantine;
import com.filrouge.dao.CantineDao;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.UtilisateurDao;

import javax.servlet.http.HttpServletRequest;

public class CantineForm {
	private Map<String, String> erreurs = new HashMap<String, String> ();
	private String resultat;
	private ArrayList<TicketCantine> tickets = new ArrayList <TicketCantine>();
	
	
	public Map<String, String> getErreurs () {
		return erreurs;
	}
	
	private void setErreur( String champ, String message ) {
	    erreurs.put( champ, message );
	}
	
	private LocalDate setLocaldate( String date ) {
		LocalDate day;
		day = LocalDate.parse(date);
	    return day;
	}
	
	public String getResultat () {
		return resultat;
	}
	
	public boolean validerFormulaire (HttpServletRequest request, EnfantDao enfantDao, Utilisateur user, CantineDao cantineDao, UtilisateurDao utilisateurDao, boolean estAdmin) {
		Utilisateur parent = null;
		boolean emailValide = true;
		if (estAdmin){
			String email = request.getParameter("email");
			emailValide = validationEmail(email, utilisateurDao);
			if (emailValide) {
				parent = utilisateurDao.trouver(email);
			}
		} else {
			parent = user;
		}
		boolean estValide = erreurs.isEmpty() ? true : false;
		
		if (emailValide){
			String enfant = request.getParameter( "enfant" );
			String[] dates = request.getParameterValues("date");
			
			validationEnfant(enfant, enfantDao, parent);
			
			estValide = erreurs.isEmpty() ? true : false;
			if (estValide){
				validationDates(dates, enfant, cantineDao);
			}
			estValide = erreurs.isEmpty() ? true : false;
			if (estValide) {
				initialiseTickets(dates, enfant, parent.getId());
				creerTickets(cantineDao);
				resultat = "Inscription effectuée";
			} else {
				resultat = "Formulaire invalide";
			}
		}
		return estValide;
	}
	
	private void creerTickets(CantineDao cantineDao) {
		for (int i = 0 ; i < tickets.size(); i++){
			cantineDao.creer(tickets.get(i));
		}
	}

	private void initialiseTickets(String[] dates, String enfant, String user) {
		LocalDate auj = new LocalDate();
		for (int i = 0; i < dates.length; i++){
			LocalDate date = setLocaldate(dates[i]);
			TicketCantine cantine = new TicketCantine();
			cantine.setAcheteur(user);
			cantine.setBeneficiaire(enfant);
			cantine.setDateAchat(auj);
			cantine.setDateUtilisation(date);
			cantine.setPrixAchat(TicketCantine.getPrixVente());
			tickets.add(cantine);
		}
	}

	private void validationEnfant (String enfant, EnfantDao enfantDao, Utilisateur user) {
		if (enfant == null || enfant.trim().length() == 0 || !enfant.matches("[0-9]+")){
			setErreur("enfant", "Ceci n'est pas votre enfant");
		} else {
			Enfant child = null;
			child =	enfantDao.trouver(enfant);
			if (child == null || (!child.getParent1().equals(user.getId()) &&  !child.getParent2().equals(user.getId()) )){
				setErreur("enfant", "Vous m'aviez caché cet enfant");
			}
		}
	}
	
	private void validationDates (String[] dates, String enfant, CantineDao cantineDao){
		for (int i = 0; i < dates.length; i++){
			if (dates[i] == null || dates[i].trim().length() == 0 || !dates[i].matches("[0-9]{4}+[-]{1}+[0-9]{2}+[-]{1}+[0-9]{2}+")){
				setErreur("date", "Pas le " + dates[i] + ", j'ai poney");
			} else {
				TicketCantine cantine = null;
				cantine = cantineDao.trouver(enfant, dates[i]);
				if (cantine != null){
					setErreur("date", "Gourmand personnage ! On ne peut manger qu'une fois le " + dates[i] +".");
				}
			}
		}
	}
	
	private boolean validationEmail (String email, UtilisateurDao utilisateurDao) {
		boolean estValide = true;
		if (email == null) {
			setErreur("email", "Merci de saisir votre adresse email");
			estValide = false;
		} else if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")){
			setErreur("email", "Merci de saisir une adresse email valide");
			estValide = false;
		} else {
			Utilisateur user = null;
			user = utilisateurDao.trouver(email);
			
			if (user == null){
				setErreur("email", "Vous n'existez pas");
				estValide = false;
			}
		}
		
		return estValide;
	}
}
