package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.UtilisateurDao;
import com.filrouge.form.InscriptionForm;

public class Inscription extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UtilisateurDao utilisateurDao;
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher( "WEB-INF/inscription.jsp" ).forward(request, response);
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur user = null;
		boolean estAdmin = false;
		user = (Utilisateur) session.getAttribute("user");
		if (user != null && user.getAccess().equals("admin")){
			estAdmin = true;
		}
		
		InscriptionForm form = new InscriptionForm();
		
		boolean estValide = form.validerFormulaire(request, estAdmin, utilisateurDao);
		
		request.setAttribute("erreurs", form.getErreurs());
		request.setAttribute("resultat", form.getResultat());
		
		if (estValide) {
			Utilisateur parent = new Utilisateur(request, estAdmin);
			utilisateurDao.creer(parent);
			if(estAdmin){
				response.sendRedirect("administration");
			} else {
				response.sendRedirect("connexion");				
			}
		} else {
			request.getRequestDispatcher( "WEB-INF/inscription.jsp" ).forward(request, response);
		}	
	}
	
	public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getUtilisateurDao();
    }
}
