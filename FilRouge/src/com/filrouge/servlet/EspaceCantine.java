package com.filrouge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.filrouge.affichage.CantineAffichage;
import com.filrouge.dao.CantineDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.fonction.PDF;
import com.itextpdf.text.DocumentException;

public class EspaceCantine extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CantineDao cantineDao;
	
	public void init() throws ServletException {
        this.cantineDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getCantineDao();
    }
	
	public void doGet  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CantineAffichage cantineAffichage = new CantineAffichage(cantineDao);
		request.setAttribute("sem1", cantineAffichage.getSem1());
		request.setAttribute("sem2", cantineAffichage.getSem2());
		try {
			@SuppressWarnings("unused")
			PDF test = new PDF(cantineAffichage.getSem1(), cantineAffichage.getSem2(), "cantine");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		LocalDate auj = new LocalDate();
		while (!auj.dayOfWeek().getAsText().equals("lundi")){
			auj = auj.minusDays(1);
		}
		String jour = auj.toString();
		String DEST = "results/tables/"+ jour + ".pdf";
		request.setAttribute("dest", DEST);
		request.getRequestDispatcher( "/WEB-INF/cantine/espace-cantine.jsp" ).forward(request, response);
	}

}
