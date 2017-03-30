package com.filrouge.bean;

public class TicketGarderie extends Ticket {
	private static double prixVente;
	private String horaire;

	public TicketGarderie() {
	}

	public static double getPrixVente() {
		return prixVente;
	}

	public static void setPrixVente(double prixVente) {
		TicketGarderie.prixVente = prixVente;
	}

	public String getHoraire() {
		return horaire;
	}

	public void setHoraire(String horaire) {
		this.horaire = horaire;
	}
}
