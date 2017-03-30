package com.filrouge.fonction;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
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

public class TaskWeek extends TimerTask {

	public void run() {
		LocalDate auj = new LocalDate();
		while (!auj.dayOfWeek().getAsText().equals("lundi")){
			auj = auj.minusDays(1);
		}
		String jour = auj.toString();
		jour += ".pdf";
		String DEST = "/Users/59013-89-06/workspace/FilRouge/WebContent/WEB-INF/results/cantine";
		final String username = "adressemail";
		final String password = "password";

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
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("louis.devergnies@gmail.com"));
			message.setSubject("Listing cantine");
//			message.setText("Veuillez trouver en pièce-jointe la liste des enfants inscrits à la cantine pour la semaine en cours, ainsi que la semaine prochaine.");
			
			
			
			File fichier = new File(DEST, jour);
		
			
			Multipart mp = new MimeMultipart();
			MimeBodyPart txt = new MimeBodyPart();
			txt.setContent("Veuillez trouver en pièce-jointe la liste des enfants inscrits à la cantine pour la semaine en cours, ainsi que la semaine prochaine.", "text/plain");
			mp.addBodyPart(txt);
			
			MimeBodyPart attachment = new MimeBodyPart();
		    
		    attachment.setFileName(jour);
		    attachment.attachFile(fichier);
		    
		    mp.addBodyPart(attachment);
		    message.setContent(mp);
//		    System.out.println("ok");
			Transport.send(message);

			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
