package com.filrouge.fonction;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.joda.time.LocalDate;

public class PDF {
	static String backslash= System.getProperty("file.separator") ;
	
	
	public PDF (ArrayList<ArrayList<String>> liste1, ArrayList<ArrayList<String>> liste2, String chemin) throws FileNotFoundException, DocumentException {
		LocalDate auj = new LocalDate();
		while (!auj.dayOfWeek().getAsText().equals("lundi")){
			auj = auj.minusDays(1);
		}
		String jour = auj.toString();
		String DEST = "/Users/59013-89-06/workspace/FilRouge/WebContent/WEB-INF/results/"+ chemin +"/"+ jour + ".pdf";
		
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		createPdf(liste1, liste2, DEST);
	}
	
	public PDF (ArrayList<ArrayList<String>> liste, String chemin) throws FileNotFoundException, DocumentException {
		LocalDate auj = new LocalDate();
		while (!auj.dayOfWeek().getAsText().equals("lundi")){
			auj = auj.minusDays(1);
		}
		String jour = auj.toString();
		String DEST = "/Users/59013-89-06/workspace/FilRouge/WebContent/WEB-INF/results/" + chemin + "/"+ jour + ".pdf";
		
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		createPdf(liste, DEST);
	}
	
	public void createPdf(ArrayList<ArrayList<String>> liste1, ArrayList<ArrayList<String>> liste2, String DEST) throws FileNotFoundException, DocumentException {
		
		Document document = new Document();
		document.setMargins(8,8, 8, 8);
		
		PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();
		PdfPTable table1 = new PdfPTable(liste1.size());
		table1.setWidthPercentage(100);
		for (ArrayList<String> sousListe : liste1) {
			PdfPTable tabletmp = new PdfPTable(1);
			for (String champ : sousListe){
					tabletmp.addCell(champ);				
			}
			PdfPCell cell = new PdfPCell(tabletmp);
			table1.addCell(cell);
		}
		document.add(table1);
		document.newPage();
		PdfPTable table2 = new PdfPTable(liste2.size());
		table2.setWidthPercentage(100);
		for (ArrayList<String> sousListe : liste2) {
			PdfPTable tabletmp = new PdfPTable(1);
			for (String champ : sousListe){
					tabletmp.addCell(champ);				
			}
			PdfPCell cell = new PdfPCell(tabletmp);
			table2.addCell(cell);
		}
		document.add(table2);
		document.close();
	}
	
public void createPdf(ArrayList<ArrayList<String>> liste, String DEST) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		document.setMargins(8,8, 8, 8);
		PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();
		PdfPTable table = new PdfPTable(liste.size());
		table.setWidthPercentage(100);
		for (ArrayList<String> sousListe : liste) {
			PdfPTable tabletmp = new PdfPTable(1);
			for (String champ : sousListe){
					tabletmp.addCell(champ);				
			}
			PdfPCell cell = new PdfPCell(tabletmp);
			table.addCell(cell);
		}
		document.add(table);
		document.close();
	}
}
