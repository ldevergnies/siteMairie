package com.filrouge.affichage;

import java.util.ArrayList;

import org.joda.time.LocalDate;
import com.filrouge.dao.GarderieDao;

public class GarderieAffichage {
	private ArrayList<ArrayList<String>> sem1 = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> sem2 = new ArrayList<ArrayList<String>>();
	private LocalDate auj;
	
	public GarderieAffichage(GarderieDao garderieDao) {
		auj = new LocalDate();
		
		switch (auj.dayOfWeek().getAsText()) {
		case "mardi" : 		auj = auj.minusDays(1);
							break;
		case "mercredi" : 		auj = auj.minusDays(2);
		break;
		case "jeudi" : 		auj = auj.minusDays(3);
							break;	
		case "vendredi" : 	auj = auj.minusDays(4);
							break;
		case "samedi" : 	auj = auj.plusDays(5);
							break;
		case "dimanche" : 	auj = auj.plusDays(6);
							break;
		default : break;
		}
		
		for (int i = 0; i < 7; i++){
			if (!auj.dayOfWeek().getAsText().equals("samedi") && !auj.dayOfWeek().getAsText().equals("dimanche")){
				sem1.add(lister(auj, garderieDao));
			}
			auj = auj.plusDays(1);
		}
		for (int i = 0; i < 7; i++){
			if (!auj.dayOfWeek().getAsText().equals("samedi") && !auj.dayOfWeek().getAsText().equals("dimanche")){
				sem2.add(lister(auj, garderieDao));
			}
			auj = auj.plusDays(1);
		}
	}

	public ArrayList<ArrayList<String>> getSem1() {
		return sem1;
	}
	public ArrayList<ArrayList<String>> getSem2() {
		return sem2;
	}

	private ArrayList<String> lister(LocalDate auj, GarderieDao garderieDao) {
		ArrayList<String> liste = new ArrayList<String>();
		ArrayList<String> listeTmp = new ArrayList<String>();
		if (!auj.dayOfWeek().getAsText().equals("mercredi") && !auj.dayOfWeek().getAsText().equals("jeudi")){
			liste = garderieDao.trouverLesEnfants(auj, "ma");
			liste.add(0, auj.dayOfWeek().getAsText() + " " + auj + " " + "ma");
			liste.add("");
		}
		listeTmp = garderieDao.trouverLesEnfants(auj, "am");
		listeTmp.add(0, auj.dayOfWeek().getAsText() + " " + auj + " " + "am");
		for (int i = 0; i < listeTmp.size(); i++) {
			liste.add(listeTmp.get(i));
		}
		return liste;
	}
}
