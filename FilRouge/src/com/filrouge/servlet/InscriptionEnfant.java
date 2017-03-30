package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.UtilisateurDao;
import com.filrouge.form.InscriptionEnfantForm;

public class InscriptionEnfant extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnfantDao enfantDao;
	private UtilisateurDao utilisateurDao;
	
	public void doGet  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher( "/WEB-INF/client/inscription_enfant.jsp" ).forward(request, response);
	}
	
	public void doPost  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur user = null;
		boolean estAdmin = false;
		user = (Utilisateur) session.getAttribute("user");
		if (user.getAccess().equals("admin")){
			estAdmin = true;
		}
		
		InscriptionEnfantForm form = new InscriptionEnfantForm(enfantDao);
		
		
		boolean estValide = form.validerFormulaire(request, estAdmin, utilisateurDao);
		
		request.setAttribute("erreurs", form.getErreurs());
		request.setAttribute("resultat", form.getResultat());
		
		if (estValide) {
			Utilisateur parent = null;
			if (estAdmin){
				parent = utilisateurDao.trouver(request.getParameter("email"));
			} else {
				parent = user;
			}
			String creer = form.creerEnfant(request, parent, session);
			request.setAttribute("creer", creer);
			
			System.out.println(creer);
			if (estAdmin){
				response.sendRedirect("/FilRouge/administration");
			} else {
				response.sendRedirect("/FilRouge/parent");
			}
			
		} else {
			request.getRequestDispatcher( "/WEB-INF/client/inscription_enfant.jsp" ).forward(request, response);
		}
	}
	
	public void init() throws ServletException {
        this.enfantDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getEnfantDao();
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getUtilisateurDao();
    }
}
