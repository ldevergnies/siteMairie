package com.filrouge.bean;

public class TicketActivite extends Ticket {
	private static double prixVenteFoot;
	private static double prixVenteCouture;
	private String horaire;
	private String type;
	public static double getPrixVenteFoot() {
		return prixVenteFoot;
	}
	public static void setPrixVenteFoot(double prixVenteFoot) {
		TicketActivite.prixVenteFoot = prixVenteFoot;
	}
	public static double getPrixVenteCouture() {
		return prixVenteCouture;
	}
	public static void setPrixVenteCouture(double prixVenteCouture) {
		TicketActivite.prixVenteCouture = prixVenteCouture;
	}
	public String getHoraire() {
		return horaire;
	}
	public void setHoraire(String horaire) {
		this.horaire = horaire;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TicketActivite() {
	}
}
