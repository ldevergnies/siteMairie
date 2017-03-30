package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.filrouge.affichage.GarderieAffichage;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.GarderieDao;
import com.filrouge.fonction.PDF;
import com.itextpdf.text.DocumentException;

public class EspaceGarderie extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GarderieDao garderieDao;
	
	public void init() throws ServletException {
		this.garderieDao = ((DAOFactory) getServletContext().getAttribute("daofactory")).getGarderieDao();
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GarderieAffichage garderie = new GarderieAffichage(garderieDao);
		request.setAttribute("sem1", garderie.getSem1());
		request.setAttribute("sem2", garderie.getSem2());
		try {
			@SuppressWarnings("unused")
			PDF test = new PDF(garderie.getSem1(), garderie.getSem2(), "garderie");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		LocalDate auj = new LocalDate();
		while (!auj.dayOfWeek().getAsText().equals("lundi")){
			auj = auj.minusDays(1);
		}
		String jour = auj.toString();
		String DEST = "results/garderie/"+ jour + ".pdf";
		request.setAttribute("dest", DEST);
		
		request.getRequestDispatcher("/WEB-INF/garderie/espace-garderie.jsp").forward(request, response);
	}
}
