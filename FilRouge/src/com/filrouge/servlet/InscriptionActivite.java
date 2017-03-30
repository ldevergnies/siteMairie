package com.filrouge.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.ActiviteDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.UtilisateurDao;
import com.filrouge.form.ActiviteForm;

public class InscriptionActivite extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnfantDao enfantDao;
	private ActiviteDao activiteDao;
	private UtilisateurDao utilisateurDao;
	
	public void init() throws ServletException {
        this.enfantDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getEnfantDao();
        this.activiteDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getActiviteDao();
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getUtilisateurDao();
    }
	
	private void initialise( HttpServletRequest request){
		HttpSession session = request.getSession();
		Utilisateur user = null;
		boolean estAdmin = false;
		user = (Utilisateur) session.getAttribute("user");
		if (user.getAccess().equals("admin")){
			estAdmin = true;
		}
		ArrayList<Enfant> enfants = new ArrayList<Enfant> ();
		ArrayList<Object> dates = new ArrayList<Object> ();
		dates = calendrier();
		if (estAdmin){
			enfants = enfantDao.parcourir();
		} else {
			enfants = enfantDao.parcourir(user);
		}
		
		request.setAttribute("dates", dates);
		request.setAttribute("enfants", enfants);
	}
	
	private ArrayList<Object> calendrier() {
		ArrayList<Object> dates = new ArrayList<Object> ();
		LocalDate auj = new LocalDate();
		auj = auj.plusDays(1);
		
		for(int i = 0; i < 7; i++){
			if(auj.dayOfWeek().getAsText().equals("mercredi") || auj.dayOfWeek().getAsText().equals("jeudi")){
				dates.add(auj);
				dates.add(auj.plusDays(7));
				dates.add(auj.plusDays(14));
				dates.add(auj.plusDays(21));
			}
			auj = auj.plusDays(1);
		}
		return dates;
	}
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		initialise(request);
		request.getRequestDispatcher("/WEB-INF/client/activite.jsp").forward(request, response);
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur user = null;
		boolean estAdmin = false;
		user = (Utilisateur) session.getAttribute("user");
		if (user.getAccess().equals("admin")){
			estAdmin = true;
		}
		
		ActiviteForm form = new ActiviteForm();
		boolean estValide;
		estValide = form.validerFormulaire(request, enfantDao, user, activiteDao, utilisateurDao, estAdmin);
		
		request.setAttribute("erreurs", form.getErreurs());
		
		if (estValide){
			if (estAdmin){
				response.sendRedirect("/FilRouge/administration");
			} else {
				response.sendRedirect("/FilRouge/parent");
			}
		}else {
			initialise (request);
			request.getRequestDispatcher( "/WEB-INF/client/activite.jsp" ).forward(request, response);
		}
	}
	
}
