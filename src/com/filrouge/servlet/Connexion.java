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
import com.filrouge.form.ConnexionForm;

public class Connexion extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UtilisateurDao utilisateurDao;
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher( "WEB-INF/connexion.jsp" ).forward(request, response);
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConnexionForm form = new ConnexionForm(utilisateurDao);
		boolean estValide = form.validerFormulaire(request);
		
		request.setAttribute("erreurs", form.getErreurs());
		request.setAttribute("resultat", form.getResultat());
		
		if (estValide){
			HttpSession session = request.getSession();
			String email = request.getParameter("adresseEmail");
			Utilisateur parent = utilisateurDao.trouver(email);
			session.setAttribute("user", parent);
			request.getRequestDispatcher("redirection").forward(request, response);
		} else {
			request.getRequestDispatcher("WEB-INF/connexion.jsp").forward(request, response);
		}
	}
	
	public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getUtilisateurDao();
    }
}
