package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.filrouge.bean.Utilisateur;

public class Redirection extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur user = null;
		user = (Utilisateur) session.getAttribute("user");
		if (user == null) {
			response.sendRedirect("connexion");
		}else if (user.getAccess().equals("admin")) {
			response.sendRedirect("administration");
		} else if (user.getAccess().equals("parent")){
			response.sendRedirect("parent");
		} else if (user.getAccess().equals("cantine")){
			response.sendRedirect("espace_cantine");
		} else if (user.getAccess().equals("garderie")){
			response.sendRedirect("espace_garderie");
		} else if (user.getAccess().equals("activite")){
			response.sendRedirect("espace_activite");
		}
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utilisateur user = (Utilisateur) session.getAttribute("user");
		if (user.getAccess().equals("admin")) {
			response.sendRedirect("administration");
		} else if (user.getAccess().equals("parent")){
			response.sendRedirect("parent");
		} else if (user.getAccess().equals("cantine")){
			response.sendRedirect("espace_cantine");
		} else if (user.getAccess().equals("garderie")){
			response.sendRedirect("espace_garderie");
		} else if (user.getAccess().equals("activite")){
			response.sendRedirect("espace_activite");
		}
	}
}
