package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filrouge.dao.ConstantesDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.form.ConstantesForm;

public class Constantes extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConstantesDao constantesDao;
	public void init() throws ServletException {
        this.constantesDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getConstantesDao();
    }
	public void doGet  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher( "/WEB-INF/administration/constantes.jsp" ).forward(request, response);
	}
	
	public void doPost  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConstantesForm form = new ConstantesForm();
		boolean estValide = form.validerForm(request, constantesDao);
		request.setAttribute("erreurs", form.getErreurs());
		request.setAttribute("resultat", form.getResultat());
		
		if (estValide){
			response.sendRedirect("/FilRouge/administration");
		} else {
			request.getRequestDispatcher( "/WEB-INF/administration/constantes.jsp" ).forward(request, response);
		}
	}
}
