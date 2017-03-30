package com.filrouge.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.bean.TicketActivite;
import com.filrouge.bean.TicketCantine;
import com.filrouge.bean.TicketGarderie;
import com.filrouge.dao.ActiviteDao;
import com.filrouge.dao.CantineDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.GarderieDao;
import com.filrouge.dao.UtilisateurDao;

public class PageUtilisateur extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnfantDao enfantDao;
	private CantineDao cantineDao;
	private GarderieDao garderieDao;
	private ActiviteDao activiteDao;
	private UtilisateurDao utilisateurDao;
	private Map<String, String> erreurs = new HashMap<String, String> ();
	
	private void setErreur( String champ, String message ) {
	    erreurs.put( champ, message );
	}
	
	public void init() throws ServletException {
        this.enfantDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getEnfantDao();
        this.cantineDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getCantineDao();
        this.activiteDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getActiviteDao();
        this.garderieDao = ( (DAOFactory) getServletContext().getAttribute("daofactory")).getGarderieDao();
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute("daofactory")).getUtilisateurDao();
    }
	
	private void initialise (HttpServletRequest request, Utilisateur user){
		ArrayList<Enfant> enfants = enfantDao.parcourir(user);
		ArrayList<TicketCantine> cantines = cantineDao.parcourir(user);
		ArrayList<TicketGarderie> garderie = garderieDao.parcourir(user);
		ArrayList<TicketActivite> activite = activiteDao.parcourir(user);
		ArrayList<Map<String,String>> ticketsCantine = new ArrayList<Map<String,String>>();
		ArrayList<Map<String,String>> ticketsGarderie = new ArrayList<Map<String,String>>();
		ArrayList<Map<String,String>> ticketsActivite = new ArrayList<Map<String,String>>();
		for (int i = 0; i < cantines.size(); i++){
			ticketsCantine.add(associerEnfant(cantines.get(i), enfants));
		}
		for (int i = 0; i < garderie.size(); i++) {
			ticketsGarderie.add(associerEnfant(garderie.get(i), enfants));
		}
		for (int i = 0; i < activite.size(); i++) {
			ticketsActivite.add(associerEnfant(activite.get(i), enfants));
		}
		
		request.setAttribute("enfants", enfants);
		request.setAttribute("cantines", ticketsCantine);
		request.setAttribute("garderie", ticketsGarderie);
		request.setAttribute("activite", ticketsActivite);
	}
	
	private Map<String,String> associerEnfant (TicketGarderie garderie, ArrayList<Enfant> enfants){
		Map<String, String> associer = new HashMap<String,String>();
		associer.put("ticket", garderie.getDateUtilisation().toString() + garderie.getHoraire());
		for (int i = 0; i < enfants.size(); i++){
			if (enfants.get(i).getId().equals(garderie.getBeneficiaire())){
				associer.put("enfant", enfants.get(i).getPrenom() + " " + enfants.get(i).getNom());
				break;
			}
		}
		return associer;
	}
	
	private Map<String,String> associerEnfant (TicketActivite activite, ArrayList<Enfant> enfants){
		Map<String, String> associer = new HashMap<String,String>();
		associer.put("ticket", activite.getDateUtilisation().toString() + " " + activite.getType());
		for (int i = 0; i < enfants.size(); i++){
			if (enfants.get(i).getId().equals(activite.getBeneficiaire())){
				associer.put("enfant", enfants.get(i).getPrenom() + " " + enfants.get(i).getNom());
				break;
			}
		}
		return associer;
	}
	
	private Map<String,String> associerEnfant (TicketCantine cantine, ArrayList<Enfant> enfants){
		Map<String, String> associer = new HashMap<String,String>();
		associer.put("ticket", cantine.getDateUtilisation().toString());
		for (int i = 0; i < enfants.size(); i++){
			if (enfants.get(i).getId().equals(cantine.getBeneficiaire())){
				associer.put("enfant", enfants.get(i).getPrenom() + " " + enfants.get(i).getNom());
				break;
			}
		}
		return associer;
	}
	
	public void doGet  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur parent = null;
		parent = (Utilisateur) session.getAttribute("user");
		if (parent == null) {
			response.sendRedirect("connexion");
		} else {
			initialise(request, parent);
			request.setAttribute("parent", parent);
			request.getRequestDispatcher( "WEB-INF/client/espace-client.jsp" ).forward(request, response);
		}
	}
	
	public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur user = null;
		user = (Utilisateur) session.getAttribute("user");
		if (user == null) {
			response.sendRedirect("connexion");
		} else {
			Utilisateur parent = null;
			String email = request.getParameter("email");
			if (validationEmail(email, utilisateurDao)){
				parent = utilisateurDao.trouver(email);
				initialise(request, parent);
				request.setAttribute("parent", parent);
				request.getRequestDispatcher( "WEB-INF/client/espace-client.jsp" ).forward(request, response);
			} else {
				request.setAttribute("erreurs", erreurs);
				request.getRequestDispatcher( "administration" ).forward(request, response);;
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
