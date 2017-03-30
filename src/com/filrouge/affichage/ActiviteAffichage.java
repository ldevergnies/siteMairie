package com.filrouge.affichage;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import com.filrouge.dao.ActiviteDao;


public class ActiviteAffichage {
	private ArrayList<ArrayList<String>> sem1 = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> sem2 = new ArrayList<ArrayList<String>>();
	private LocalDate auj;
	
	public ArrayList<ArrayList<String>> getSem1() {
		return sem1;
	}
	public ArrayList<ArrayList<String>> getSem2() {
		return sem2;
	}
	
	public ActiviteAffichage(ActiviteDao activiteDao) {
		auj = new LocalDate();
		
		switch (auj.dayOfWeek().getAsText()) {
		
		case "vendredi" : 	auj = auj.minusDays(2);
							break;
		case "samedi" : 	auj = auj.minusDays(3);
							break;
		case "dimanche" : 	auj = auj.minusDays(4);
							break;
		case "lundi" : 		auj = auj.plusDays(2);
							break;
		case "mardi" : 		auj = auj.plusDays(1);
							break;
		case "jeudi" : 		auj = auj.minusDays(1);
							break;	
		default : break;
		}
		
		for (int i = 0; i < 2; i++){
			sem1.add(lister(auj, activiteDao));
			auj = auj.plusDays(1);
		}
		auj = auj.plusDays(5);
		for (int i = 0; i < 2; i++){
			sem2.add(lister(auj, activiteDao));
			auj = auj.plusDays(1);
		}
	}
	
	private ArrayList<String> lister(LocalDate auj, ActiviteDao activiteDao) {
		ArrayList<String> liste = new ArrayList<String>();
		
		liste = activiteDao.trouverLesEnfants(auj);
		liste.add(0, auj.dayOfWeek().getAsText() + " " + auj);
		liste.add("");
		
		return liste;
	}
}
