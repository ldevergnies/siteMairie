package com.filrouge.init;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.filrouge.bean.TicketActivite;
import com.filrouge.bean.TicketCantine;
import com.filrouge.bean.TicketGarderie;
import com.filrouge.dao.ConstantesDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.fonction.TaskWeek;
import com.filrouge.fonction.TaskYear;

public class Init implements ServletContextListener {
	private static final String ATT_DAO_FACTORY = "daofactory";
	private DAOFactory daoFactory;

	public void contextDestroyed(ServletContextEvent event) {       
	}
	
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
        this.daoFactory = DAOFactory.getInstance();
        servletContext.setAttribute( ATT_DAO_FACTORY, this.daoFactory );
        ConstantesDao constantesDao = this.daoFactory.getConstantesDao();
        double prixCantine = constantesDao.trouver("prix_cantine");
        TicketCantine.setPrixVente(prixCantine);
        double prixGarderie = constantesDao.trouver("prix_garderie");
        TicketGarderie.setPrixVente(prixGarderie);
        double prixFoot = constantesDao.trouver("prix_foot");
        TicketActivite.setPrixVenteFoot(prixFoot);
        double prixCouture = constantesDao.trouver("prix_couture");
        TicketActivite.setPrixVenteCouture(prixCouture);
        LocalDateTime dateTime = constantesDao.trouverHeure("date_year");
                
		Timer timer = new Timer();
		Date firstTime ;
		LocalDate auj = new LocalDate();
		switch (auj.dayOfWeek().getAsText()) {
		case "jeudi" : 		auj = auj.plusDays(6);
							break;
		case "vendredi" : 	auj = auj.plusDays(5);
							break;
		case "samedi" : 	auj = auj.plusDays(4);
							break;
		case "dimanche" : 	auj = auj.plusDays(3);
							break;
		case "lundi" : 		auj = auj.plusDays(2);
							break;
		case "mardi" : 		auj = auj.plusDays(1);
							break;	
		default : break;
		}
		LocalDateTime test = new LocalDateTime(auj.getYear(), auj.getMonthOfYear(), auj.getDayOfMonth(), 05, 00);
		firstTime = test.toDate();
		timer.schedule(new TaskWeek(), firstTime, 1000*60*60*24*7);
		
		Timer timer2 = new Timer();
		Date firstTime2 ;
		TaskYear.date = dateTime;
		firstTime2 = dateTime.toDate();
		timer2.schedule(new TaskYear(), firstTime2);
		
	}
}
