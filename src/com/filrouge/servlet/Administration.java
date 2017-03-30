package com.filrouge.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;

import com.filrouge.dao.ActiviteDao;
import com.filrouge.dao.CantineDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.GarderieDao;

public class Administration extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CantineDao cantineDao;
	private GarderieDao garderieDao;
	private ActiviteDao activiteDao;
	
	public void init() throws ServletException {
        this.cantineDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getCantineDao();
        this.garderieDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getGarderieDao();
        this.activiteDao = ( (DAOFactory) getServletContext().getAttribute( "daofactory" ) ).getActiviteDao();
    }
	
	private void initialise(HttpServletRequest request) {
		LocalDate auj = new LocalDate();
		ArrayList<String> cantine = cantineDao.trouverLesEnfants(auj);
		ArrayList<String> garderiema = garderieDao.trouverLesEnfants(auj, "ma");
		ArrayList<String> garderieam = garderieDao.trouverLesEnfants(auj, "am");
		ArrayList<String> activite = activiteDao.trouverLesEnfants(auj);
		cantine.add(0, auj.dayOfWeek().getAsText() + " " + auj);
		garderiema.add(0, auj.dayOfWeek().getAsText() + " " + auj + " ma");
		garderieam.add(0, auj.dayOfWeek().getAsText() + " " + auj + " am");
		activite.add(0, auj.dayOfWeek().getAsText() + " " + auj);
		request.setAttribute("cantine", cantine);
		request.setAttribute("garderiema", garderiema);
		request.setAttribute("garderieam", garderieam);
		request.setAttribute("activite", activite);
	}
	
	public void doGet  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initialise(request);
		request.getRequestDispatcher( "WEB-INF/administration/espace-administration.jsp" ).forward(request, response);
	}
	public void doPost  (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initialise(request);
		request.getRequestDispatcher( "WEB-INF/administration/espace-administration.jsp" ).forward(request, response);
	}
}
