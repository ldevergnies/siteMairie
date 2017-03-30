package com.filrouge.fonction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;
import com.filrouge.dao.ActiviteDao;
import com.filrouge.dao.CantineDao;
import com.filrouge.dao.ConstantesDao;
import com.filrouge.dao.DAOFactory;
import com.filrouge.dao.EnfantDao;
import com.filrouge.dao.GarderieDao;
import com.filrouge.dao.UtilisateurDao;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TaskYear extends TimerTask {
	private DAOFactory daoFactory;
	private UtilisateurDao utilisateurDao;
	private ConstantesDao constantesDao;
	private EnfantDao enfantDao;
	private CantineDao cantineDao;
	private GarderieDao garderieDao;
	private ArrayList<Utilisateur> parents = new ArrayList<Utilisateur>();
	private ActiviteDao activiteDao;
	public static LocalDateTime date = null;
	
	public void run() {
		initialiseDao();
		
		parents = utilisateurDao.parcourir();
		
		for (Utilisateur parent : parents){
			
			ArrayList<Enfant> enfants = enfantDao.parcourir(parent);
			ArrayList<Map<String, Object>> cantine = cantine(parent, enfants);
			ArrayList<Map<String, Object>> garderie = garderie(parent, enfants);
			ArrayList<Map<String, Object>> activite = activite(parent, enfants);
			
			try {
				creerPdf(parent, enfants, cantine, garderie, activite);
			} catch (FileNotFoundException | DocumentException e) {
				e.printStackTrace();
			}
			
			sendMail(parent);
			
		}

		int year = date.getYear() + 1;
		date = new LocalDateTime(year, 06, 30, 0, 0);
		constantesDao.modifier(date, "date_year");
		Date firstTime3 = TaskYear.date.toDate();
		Timer timer3 = new Timer();
		timer3.schedule(new TaskYear(), firstTime3);
	}
	
	private void sendMail(Utilisateur parent){
		LocalDate auj = new LocalDate();
		String jour = Integer.toString(auj.getYear());
		String DEST = "/Users/59013-89-06/workspace/FilRouge/WebContent/WEB-INF/results/"+ jour;
		String NAME = parent.getNom() + parent.getPrenom() + ".pdf";
		final String username = "louis.devergnies@gmail.com";
		final String password = "XDjlbjej";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("louis.devergnies@gmail.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(parent.getAdresseEmail()));
			message.setSubject("Listing cantine");
			
			Multipart mp = new MimeMultipart();
			
			MimeBodyPart txt = new MimeBodyPart();
			txt.setContent("Veuillez trouver en pièce-jointe votre factur annuelle.", "text/plain");
			mp.addBodyPart(txt);
			
			MimeBodyPart attachment = new MimeBodyPart();
			File fichier = new File(DEST, NAME);
		    attachment.setFileName(NAME);
		    attachment.attachFile(fichier);
		    
		    mp.addBodyPart(attachment);
		    message.setContent(mp);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void creerPdf(Utilisateur parent, ArrayList<Enfant> enfants, ArrayList<Map<String, Object>> cantine, ArrayList<Map<String, Object>> garderie, ArrayList<Map<String, Object>> activite) throws FileNotFoundException, DocumentException {
		LocalDate auj = new LocalDate();
		String jour = Integer.toString(auj.getYear());
		String DEST = "/Users/59013-89-06/workspace/FilRouge/WebContent/WEB-INF/results/"+ jour +"/"+ parent.getNom() + parent.getPrenom() + ".pdf";
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();
		
		PdfPTable tableParent = new PdfPTable(1);
		tableParent.setWidthPercentage(100/4);
		tableParent.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableParent.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableParent.addCell(parent.getNom() + " " + parent.getPrenom());
		tableParent.addCell(parent.getAdresse());
		tableParent.addCell(parent.getCodePostal() + " " + parent.getVille());
		tableParent.addCell(parent.getAdresseEmail());
		tableParent.addCell(parent.getNumeroTelephone());
		document.add(tableParent);
		
		PdfPTable tableEnfant = new PdfPTable(1);
		tableEnfant.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableEnfant.setWidthPercentage(100/4);
		tableEnfant.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableEnfant.addCell("Enfant(s) :");
		for (int i = 0; i < enfants.size(); i++){
			tableEnfant.addCell(enfants.get(i).getPrenom() + " " + enfants.get(i).getNom());
		}
		document.add(tableEnfant);
		
		PdfPTable tableCantine = new PdfPTable(3);
		tableCantine.setWidthPercentage(100);
		tableCantine.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableCantine.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableCantine.setWidths(new float[]{31.0f, 57.5f, 11.5f});
		tableCantine.setSpacingBefore(50);
		tableCantine.addCell("Ticket de cantine");
		tableCantine.addCell("Nombre");
		tableCantine.addCell("Prix");
		for (int i = 0; i < cantine.size(); i++) {
			tableCantine.addCell((String) cantine.get(i).get("nom"));
			tableCantine.addCell(((Integer) cantine.get(i).get("nombre")).toString());
			tableCantine.addCell(((BigDecimal) cantine.get(i).get("prix")).toString());
		}
		document.add(tableCantine);
		
		PdfPTable tableGarderie = new PdfPTable(7);
		tableGarderie.setWidthPercentage(100);
		tableGarderie.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableGarderie.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableGarderie.setWidths(new float[]{31.0f, 11.5f, 11.5f, 11.5f, 11.5f, 11.5f, 11.5f});
		tableGarderie.setSpacingBefore(25);
		tableGarderie.addCell("Ticket de garderie");
		tableGarderie.addCell("Matin");
		tableGarderie.addCell("Prix");
		tableGarderie.addCell("Après-midi");
		tableGarderie.addCell("Prix");
		tableGarderie.addCell("Nombre");
		tableGarderie.addCell("Prix");
		for (int i = 0; i < cantine.size(); i++) {
			tableGarderie.addCell((String) garderie.get(i).get("nom"));
			tableGarderie.addCell(((Integer) garderie.get(i).get("nbMa")).toString());
			tableGarderie.addCell(((BigDecimal) garderie.get(i).get("pxMa")).toString());
			tableGarderie.addCell(((Integer) garderie.get(i).get("nbAm")).toString());
			tableGarderie.addCell(((BigDecimal) garderie.get(i).get("pxAm")).toString());
			tableGarderie.addCell(((Integer) garderie.get(i).get("nombre")).toString());
			tableGarderie.addCell(((BigDecimal) garderie.get(i).get("prix")).toString());
		}
		document.add(tableGarderie);
		
		PdfPTable tableActivite = new PdfPTable(7);
		tableActivite.setWidthPercentage(100);
		tableActivite.setHorizontalAlignment(Element.ALIGN_LEFT);
		tableActivite.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableActivite.setWidths(new float[]{31.0f, 11.5f, 11.5f, 11.5f, 11.5f, 11.5f, 11.5f});
		tableActivite.setSpacingBefore(25);
		tableActivite.addCell("Ticket d'activite");
		tableActivite.addCell("Foot");
		tableActivite.addCell("Prix");
		tableActivite.addCell("Couture");
		tableActivite.addCell("Prix");
		tableActivite.addCell("Nombre");
		tableActivite.addCell("Prix");
		for (int i = 0; i < cantine.size(); i++) {
			tableActivite.addCell((String) activite.get(i).get("nom"));
			tableActivite.addCell(((Integer) activite.get(i).get("nbFoot")).toString());
			tableActivite.addCell(((BigDecimal) activite.get(i).get("pxFoot")).toString());
			tableActivite.addCell(((Integer) activite.get(i).get("nbCouture")).toString());
			tableActivite.addCell(((BigDecimal) activite.get(i).get("pxCouture")).toString());
			tableActivite.addCell(((Integer) activite.get(i).get("nombre")).toString());
			tableActivite.addCell(((BigDecimal) activite.get(i).get("prix")).toString());
		}
		document.add(tableActivite);
		
		PdfPTable tableTotal = new PdfPTable(2);
		tableTotal.setWidthPercentage(100);
		tableTotal.setSpacingBefore(50);
		tableTotal.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableTotal.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		tableTotal.setWidths(new float[]{88.5f, 11.5f});
		tableTotal.addCell(new Phrase("PRIX TOTAL", new Font(Font.FontFamily.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD)));
		BigDecimal total = ((BigDecimal) cantine.get(cantine.size()-1).get("prix")).add(((BigDecimal) garderie.get(garderie.size()-1).get("prix")).add((BigDecimal) activite.get(activite.size()-1).get("prix")));
		tableTotal.addCell(new Phrase(total.toString(), new Font(Font.FontFamily.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD)));
		document.add(tableTotal);
		
		document.close();
	}
	
	private void initialiseDao() {
		this.daoFactory = new DAOFactory();
		this.utilisateurDao = daoFactory.getUtilisateurDao();
		this.enfantDao = daoFactory.getEnfantDao();
		this.cantineDao = daoFactory.getCantineDao();
		this.garderieDao = daoFactory.getGarderieDao();
		this.activiteDao = daoFactory.getActiviteDao();
		this.constantesDao = daoFactory.getConstantesDao();
	}
	
	private ArrayList<Map<String, Object>> cantine (Utilisateur parent, ArrayList<Enfant> enfants) {
		ArrayList<Map<String, Object>> Cantine = new ArrayList<Map<String, Object>>();
		int nbTicketCantine = 0;
		BigDecimal prixTotalCantine = new BigDecimal(0.00);
		for (Enfant enfant : enfants) {
			ArrayList<BigDecimal> ListeCantine = cantineDao.envoiAnnuel(parent.getId(), enfant.getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nom", enfant.getPrenom() + " " + enfant.getNom());
			map.put("nombre", ListeCantine.size());
			BigDecimal prix = new BigDecimal(0.00);
			for (int i = 0; i<ListeCantine.size(); i++){
				prix = prix.add(ListeCantine.get(i));
			}
			map.put("prix", prix);
			prixTotalCantine = prixTotalCantine.add(prix);
			nbTicketCantine += ListeCantine.size();
			Cantine.add(map);
		}
		Cantine.add(new HashMap<String,Object>());
		Cantine.get(Cantine.size()-1).put("nom", "");
		Cantine.get(Cantine.size()-1).put("nombre", nbTicketCantine);
		Cantine.get(Cantine.size()-1).put("prix", prixTotalCantine);
		return Cantine;
	}
	
	private ArrayList<Map<String, Object>> garderie (Utilisateur parent, ArrayList<Enfant> enfants) {
		ArrayList<Map<String, Object>> Garderie = new ArrayList<Map<String, Object>>();
		int nbTicketGarderie = 0;
		int nbTicketGarderieMa = 0;
		int nbTicketGarderieAm = 0;
		BigDecimal prixTotalGarderie = new BigDecimal(0.00);
		BigDecimal prixTotalGarderieMa = new BigDecimal(0.00);
		BigDecimal prixTotalGarderieAm = new BigDecimal(0.00);
		for (Enfant enfant : enfants) {
			ArrayList<Map<String, Object>> ListeGarderie = garderieDao.envoiAnnuel(parent.getId(), enfant.getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nom", enfant.getPrenom() + " " + enfant.getNom());
			int nbTicketMa = 0;
			int nbTicketAm = 0;
			BigDecimal prixGarderieMa = new BigDecimal(0.00);
			BigDecimal prixGarderieAm = new BigDecimal(0.00);
			for (int i = 0; i<ListeGarderie.size(); i++){
				if (ListeGarderie.get(i).get("horaire").equals("ma")) {
					nbTicketMa ++;
					prixGarderieMa = prixGarderieMa.add((BigDecimal) ListeGarderie.get(i).get("prix"));
				} else {
					nbTicketAm ++;
					prixGarderieAm = prixGarderieAm.add((BigDecimal) ListeGarderie.get(i).get("prix"));
				}
			}
			map.put("nbMa", nbTicketMa);
			map.put("nbAm", nbTicketAm);
			map.put("pxMa", prixGarderieMa);
			map.put("pxAm", prixGarderieAm);
			map.put("nombre", nbTicketMa + nbTicketAm);
			map.put("prix", prixGarderieAm.add(prixGarderieMa));
			nbTicketGarderie += nbTicketMa + nbTicketAm;
			nbTicketGarderieMa += nbTicketMa;
			nbTicketGarderieAm += nbTicketAm;
			prixTotalGarderie = prixTotalGarderie.add(prixGarderieAm.add(prixGarderieMa));
			prixTotalGarderieMa = prixTotalGarderieMa.add(prixGarderieMa);
			prixTotalGarderieAm = prixTotalGarderieAm.add(prixGarderieAm);
			Garderie.add(map);
		}
		Garderie.add(new HashMap<String,Object>());
		Garderie.get(Garderie.size()-1).put("nom", "");
		Garderie.get(Garderie.size()-1).put("nbMa", nbTicketGarderieMa);
		Garderie.get(Garderie.size()-1).put("nbAm", nbTicketGarderieAm);
		Garderie.get(Garderie.size()-1).put("pxMa", prixTotalGarderieMa);
		Garderie.get(Garderie.size()-1).put("pxAm", prixTotalGarderieAm);
		Garderie.get(Garderie.size()-1).put("nombre", nbTicketGarderie);
		Garderie.get(Garderie.size()-1).put("prix", prixTotalGarderie);
		return Garderie;
	}
	
	private ArrayList<Map<String, Object>> activite (Utilisateur parent, ArrayList<Enfant> enfants) {
		ArrayList<Map<String, Object>> Activite = new ArrayList<Map<String, Object>>();
		int nbTicketActivite = 0;
		int nbTicketActiviteFoot = 0;
		int nbTicketActiviteCouture = 0;
		BigDecimal prixTotalActivite = new BigDecimal(0.00);
		BigDecimal prixTotalActiviteFoot = new BigDecimal(0.00);
		BigDecimal prixTotalActiviteCouture = new BigDecimal(0.00);
		for (Enfant enfant : enfants) {
			ArrayList<Map<String, Object>> ListeActivite = activiteDao.envoiAnnuel(parent.getId(), enfant.getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nom", enfant.getPrenom() + " " + enfant.getNom());
			int nbTicketFoot = 0;
			int nbTicketCouture = 0;
			BigDecimal prixActiviteFoot = new BigDecimal(0.00);
			BigDecimal prixActiviteCouture = new BigDecimal(0.00);
			for (int i = 0; i<ListeActivite.size(); i++){
				if (ListeActivite.get(i).get("type").equals("foot")) {
					nbTicketFoot ++;
					prixActiviteFoot = prixActiviteFoot.add((BigDecimal) ListeActivite.get(i).get("prix"));
				} else {
					nbTicketCouture ++;
					prixActiviteCouture = prixActiviteCouture.add((BigDecimal) ListeActivite.get(i).get("prix"));
				}
			}
			map.put("nbFoot", nbTicketFoot);
			map.put("nbCouture", nbTicketCouture);
			map.put("pxFoot", prixActiviteFoot);
			map.put("pxCouture", prixActiviteCouture);
			map.put("nombre", nbTicketFoot + nbTicketCouture);
			map.put("prix", prixActiviteFoot.add(prixActiviteCouture));
			nbTicketActivite += nbTicketFoot + nbTicketCouture;
			nbTicketActiviteFoot += nbTicketFoot;
			nbTicketActiviteCouture += nbTicketCouture;
			prixTotalActivite = prixTotalActivite.add(prixActiviteCouture.add(prixActiviteFoot));
			prixTotalActiviteFoot = prixTotalActiviteFoot.add(prixActiviteFoot);
			prixTotalActiviteCouture = prixTotalActiviteCouture.add(prixActiviteCouture);
			Activite.add(map);
		}
		Activite.add(new HashMap<String,Object>());
		Activite.get(Activite.size()-1).put("nom", "");
		Activite.get(Activite.size()-1).put("nbFoot", nbTicketActiviteFoot);
		Activite.get(Activite.size()-1).put("nbCouture", nbTicketActiviteCouture);
		Activite.get(Activite.size()-1).put("pxFoot", prixTotalActiviteFoot);
		Activite.get(Activite.size()-1).put("pxCouture", prixTotalActiviteCouture);
		Activite.get(Activite.size()-1).put("nombre", nbTicketActivite);
		Activite.get(Activite.size()-1).put("prix", prixTotalActivite);
		return Activite;
	}
		
}
