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
import com.filrouge.bean.TicketActivite;
import com.filrouge.dao.ActiviteDao;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.UtilisateurDao;

public class ActiviteForm {

	private Map<String, String> erreurs = new HashMap<String, String> ();
	private String resultat;
	private ArrayList<TicketActivite> tickets = new ArrayList <TicketActivite>();
	
	
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
	
	private void validationType (String type){
		if (!type.equals("foot") && !type.equals("couture")){
			setErreur("activite", "On fait pas ça ici !");
		}
	}
	
	private void validationDates (String date, String horaire, String enfant, ActiviteDao activiteDao){
		if (!horaire.equals("ma") && !horaire.equals("am")){
			setErreur("date", "Ce n'est pas une heure !");
		} else if (date == null || date.trim().length() == 0 || !date.matches("[0-9]{4}+[-]{1}+[0-9]{2}+[-]{1}+[0-9]{2}+")){
			setErreur("date", "Pas le " + date + ", j'ai poney");
		} else {
			TicketActivite activite = null;
			activite = activiteDao.trouver(enfant, date, horaire);
			if (activite != null){
				setErreur("date", "Il va y avoir beaucoup de toi le " + date + " " + horaire +".");
			}
		}
	
}

	public boolean validerFormulaire(HttpServletRequest request, EnfantDao enfantDao, Utilisateur user, ActiviteDao activiteDao, UtilisateurDao utilisateurDao, boolean estAdmin) {
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
			System.out.println(enfant);
			validationEnfant(enfant, enfantDao, parent);
			estValide = erreurs.isEmpty() ? true : false;
			
			String type = request.getParameter( "activite" );
			validationType(type);
			System.out.println(type);
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
			parametres.remove("activite");
			if (estAdmin){
				parametres.remove("email");
			}
			Set<String> cle = parametres.keySet();
			if (estValide){
				Iterator<String> it = cle.iterator();
				System.out.println("***");
				while(it.hasNext()){
					String date = (String) it.next();
					System.out.println(date);
					if (parametres.get(date) != null) {
						for (int i = 0; i < parametres.get(date).length; i++){
							String horaire = parametres.get(date)[i];
							System.out.println(horaire);
							validationDates(date, horaire, enfant, activiteDao);
						}
					}
				}
			}
			
			estValide = erreurs.isEmpty() ? true : false;
			if (estValide) {
				Iterator<String> it = cle.iterator();
				try {
					while(it.hasNext()){
						String date = (String) it.next();
						for (int i = 0; i < parametres.get(date).length; i++){
							String horaire = parametres.get(date)[i];
							initialiseTicket(date, horaire, enfant, parent.getId(), type);
						}
					}
				} catch (Exception e) {
					System.out.println("zut");
				}
				creerTickets(activiteDao);
				resultat = "Inscription effectuée";
				System.out.println(resultat);
			} else {
				resultat = "Formulaire invalide";
			}
		}
		return estValide;
	}
	
	private void creerTickets(ActiviteDao activiteDao) {
		System.out.println("taille : " + tickets.size());
		for (int i = 0 ; i < tickets.size(); i++){
			activiteDao.creer(tickets.get(i));
		}
	}
	
	private void initialiseTicket(String day, String horaire, String enfant, String user, String type) {
		LocalDate auj = new LocalDate();
		LocalDate date = setLocaldate(day);
		TicketActivite activite = new TicketActivite();
		activite.setAcheteur(user);
		activite.setBeneficiaire(enfant);
		activite.setDateAchat(auj);
		activite.setDateUtilisation(date);
		activite.setType(type);
		if (activite.getType().equals("foot")){
			activite.setPrixAchat(TicketActivite.getPrixVenteFoot());
		} else {
			activite.setPrixAchat(TicketActivite.getPrixVenteCouture());
		}
		activite.setHoraire(horaire);
		tickets.add(activite);
	}

}
