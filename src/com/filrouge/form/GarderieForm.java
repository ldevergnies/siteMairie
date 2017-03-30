package com.filrouge.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.bean.TicketGarderie;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.GarderieDao;
import com.filrouge.dao.UtilisateurDao;

public class GarderieForm {
	private Map<String, String> erreurs = new HashMap<String, String> ();
	private String resultat;
	private ArrayList<TicketGarderie> tickets = new ArrayList <TicketGarderie>();
	
	
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
	
	public boolean validerFormulaire (HttpServletRequest request, EnfantDao enfantDao, Utilisateur user, GarderieDao garderieDao, UtilisateurDao utilisateurDao, boolean estAdmin) {
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
			validationEnfant(enfant, enfantDao, parent);
			estValide = erreurs.isEmpty() ? true : false;
			
			
			Map<String, String[]> parametresTmp = request.getParameterMap();
			Set<String> cleTmp = parametresTmp.keySet();
			Map<String, String[]> parametres = new HashMap<String, String[]>();
			Iterator<String> itTmp = cleTmp.iterator();
			while (itTmp.hasNext()){
				String param = (String) itTmp.next();
				parametres.put(param, parametresTmp.get(param));
			}
			parametres.remove("enfant");
			if (estAdmin){
				parametres.remove("email");
			}
			Set<String> cle = parametres.keySet();
			if (estValide){
				Iterator<String> it = cle.iterator();
				while(it.hasNext()){
					String date = (String) it.next();
					if (parametres.get(date) != null) {
						for (int i = 0; i < parametres.get(date).length; i++){
							String horaire = parametres.get(date)[i];
							validationDates(date, horaire, enfant, garderieDao);
						}
					}
				}
			}
			
			estValide = erreurs.isEmpty() ? true : false;
			if (estValide) {
				Iterator<String> it = cle.iterator();
				while(it.hasNext()){
					String date = (String) it.next();
					for (int i = 0; i < parametres.get(date).length; i++){
						String horaire = parametres.get(date)[i];
						initialiseTicket(date, horaire, enfant, parent.getId());
					
					}
				}
				creerTickets(garderieDao);
				resultat = "Inscription effectuée";
			} else {
				resultat = "Formulaire invalide";
			}
		}
		return estValide;
	}
	
	private void creerTickets(GarderieDao garderieDao) {
		for (int i = 0 ; i < tickets.size(); i++){
			garderieDao.creer(tickets.get(i));
		}
	}

	private void initialiseTicket(String day, String horaire, String enfant, String user) {
		LocalDate auj = new LocalDate();
		LocalDate date = setLocaldate(day);
		TicketGarderie garderie = new TicketGarderie();
		garderie.setAcheteur(user);
		garderie.setBeneficiaire(enfant);
		garderie.setDateAchat(auj);
		garderie.setDateUtilisation(date);
		garderie.setPrixAchat(TicketGarderie.getPrixVente());
		garderie.setHoraire(horaire);
		tickets.add(garderie);
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
	
	private void validationDates (String date, String horaire, String enfant, GarderieDao garderieDao){
			if (!horaire.equals("ma") && !horaire.equals("am")){
				setErreur("date", "Ce n'est pas une heure pour la garderie !");
			} else if (date == null || date.trim().length() == 0 || !date.matches("[0-9]{4}+[-]{1}+[0-9]{2}+[-]{1}+[0-9]{2}+")){
				setErreur("date", "Pas le " + date + ", j'ai poney");
			} else {
				TicketGarderie garderie = null;
				garderie = garderieDao.trouver(enfant, date, horaire);
				if (garderie != null){
					setErreur("date", "Il va y avoir beaucoup de toi le " + date + " " + horaire +".");
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
