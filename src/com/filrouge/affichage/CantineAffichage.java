package com.filrouge.affichage;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import com.filrouge.dao.CantineDao;

public class CantineAffichage {
	private ArrayList<ArrayList<String>> sem1 = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> sem2 = new ArrayList<ArrayList<String>>();
	private LocalDate auj;
	private boolean afficheSem2;
	
	public CantineAffichage(CantineDao cantineDao) {
		auj = new LocalDate();
		
		switch (auj.dayOfWeek().getAsText()) {
		case "lundi" :		afficheSem2 = true;
							break;
		case "mardi" :		afficheSem2 = true;
							auj = auj.minusDays(1);
							break;
		case "mercredi" :	afficheSem2 = true;
							auj = auj.minusDays(2);
							break;
		case "jeudi" : 		afficheSem2 = true;
							auj = auj.minusDays(3);
							break;
		case "vendredi" : 	afficheSem2 = true;
							auj = auj.minusDays(4);
							break;
		case "samedi" : 	afficheSem2 = true;
							auj = auj.minusDays(5);
							break;
		case "dimanche" : 	afficheSem2 = true;
							auj = auj.minusDays(6);
							break;
		}
		
		for (int i = 0; i < 5; i++){
			sem1.add(lister(auj, cantineDao));
			auj = auj.plusDays(1);
		}
		if (afficheSem2){
			auj = auj.plusDays(2);
			for (int i = 0; i < 5; i++){
				sem2.add(lister(auj, cantineDao));
				auj = auj.plusDays(1);
			}
		}
	}

	public ArrayList<ArrayList<String>> getSem1() {
		return sem1;
	}

	public ArrayList<ArrayList<String>> getSem2() {
		return sem2;
	}

	private ArrayList<String> lister(LocalDate auj, CantineDao cantineDao) {
		ArrayList<String> liste = new ArrayList<String>();
		liste = cantineDao.trouverLesEnfants(auj);
		liste.add(0, auj.dayOfWeek().getAsText() + " " + auj);
		return liste;
	}
	
}
