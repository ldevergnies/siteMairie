package com.filrouge.bean;

public class TicketCantine extends Ticket {
	private static double prixVente;
	
	public static double getPrixVente() {
		return prixVente;
	}

	public static void setPrixVente(double prixVente) {
		TicketCantine.prixVente = prixVente;
	}

	public TicketCantine(){
	}

}
