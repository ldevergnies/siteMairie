package com.filrouge.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.CantineDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.UtilisateurDao;
import com.filrouge.form.CantineForm;

import org.joda.time.*;

public class InscriptionCantine extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnfantDao enfantDao;
	private CantineDao cantineDao;
	private UtilisateurDao utilisateurDao;
	
	public void init() throws ServletException {
        this.enfantDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getEnfantDao();
        this.cantineDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getCantineDao();
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
		ArrayList<LocalDate> dates = new ArrayList<LocalDate> ();
		dates = calendrier();
		if (estAdmin){
			enfants = enfantDao.parcourir();
		} else {
			enfants = enfantDao.parcourir(user);
		}
		
		request.setAttribute("dates", dates);
		request.setAttribute("enfants", enfants);
	}
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initialise (request);
		request.getRequestDispatcher( "/WEB-INF/client/cantine.jsp" ).forward(request, response);
	}
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initialise (request);
		HttpSession session = request.getSession();
		Utilisateur user = null;
		boolean estAdmin = false;
		user = (Utilisateur) session.getAttribute("user");
		if (user.getAccess().equals("admin")){
			estAdmin = true;
		}
		
		CantineForm form = new CantineForm();
		boolean estValide;
		estValide = form.validerFormulaire(request, enfantDao, user, cantineDao, utilisateurDao, estAdmin);
		
		request.setAttribute("erreurs", form.getErreurs());
		
		if (estValide){
			if (estAdmin ){
				response.sendRedirect("/FilRouge/administration");
			} else {
				response.sendRedirect("/FilRouge/parent");
			}
		}else {
			initialise (request);
			request.getRequestDispatcher( "/WEB-INF/client/cantine.jsp" ).forward(request, response);
		}
    }
	
	private ArrayList<LocalDate> calendrier() {
		ArrayList<LocalDate> dates = new ArrayList<LocalDate> ();
		LocalDate auj = new LocalDate();
		if(!auj.dayOfWeek().getAsText().equals("lundi") && !auj.dayOfWeek().getAsText().equals("mardi")){
			auj=auj.plusDays(7);
		}
		do{
			auj = auj.plusDays(1);
		}while(!auj.dayOfWeek().getAsText().equals("lundi"));
		for(int i = 0; i < 28; i++){
			if(!auj.dayOfWeek().getAsText().equals("dimanche") && !auj.dayOfWeek().getAsText().equals("samedi")){
				dates.add(auj);
			}
			auj = auj.plusDays(1);
		}
		return dates;
	}
}
