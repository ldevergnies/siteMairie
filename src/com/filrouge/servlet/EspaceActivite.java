package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.filrouge.affichage.ActiviteAffichage;
import com.filrouge.dao.ActiviteDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.fonction.PDF;
import com.itextpdf.text.DocumentException;

public class EspaceActivite extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActiviteDao activiteDao;
	
	public void init() throws ServletException {
        this.activiteDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getActiviteDao();
    }
	
	public void doGet  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ActiviteAffichage act = new ActiviteAffichage(activiteDao);
		request.setAttribute("sem1", act.getSem1());
		request.setAttribute("sem2", act.getSem2());
		try {
			@SuppressWarnings("unused")
			PDF test = new PDF(act.getSem1(), act.getSem2(), "activite");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		LocalDate auj = new LocalDate();
		while (!auj.dayOfWeek().getAsText().equals("lundi")){
			auj = auj.minusDays(1);
		}
		String jour = auj.toString();
		String DEST = "results/activite/"+ jour + ".pdf";
		request.setAttribute("dest", DEST);
		request.getRequestDispatcher( "/WEB-INF/periscolaire/espace-activite.jsp" ).forward(request, response);
	}
}
