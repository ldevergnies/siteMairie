package com.filrouge.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Download extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String chemin = this.getServletConfig().getInitParameter( "chemin" );
		String fichierRequis = request.getPathInfo();
		if ( fichierRequis == null || "/".equals( fichierRequis ) ) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		fichierRequis = URLDecoder.decode( fichierRequis, "UTF-8");
		System.out.println(chemin);
		File fichier = new File(chemin, fichierRequis);
		System.out.println(fichier.getAbsoluteFile());
		if ( !fichier.exists() ) {
			System.out.println("perdu");
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		String type = getServletContext().getMimeType( fichier.getName() );
		System.out.println(type);
		if ( type == null ) {
		    type = "application/octet-stream";
		}
		response.reset();
		response.setBufferSize( DEFAULT_BUFFER_SIZE );
		response.setContentType( type );
		response.setHeader( "Content-Length", String.valueOf( fichier.length() ) );
		response.setHeader( "Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"" );
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		try {
		    entree = new BufferedInputStream( new FileInputStream( fichier ), DEFAULT_BUFFER_SIZE );
		    sortie = new BufferedOutputStream( response.getOutputStream(), DEFAULT_BUFFER_SIZE );
		    byte[] tampon = new byte[DEFAULT_BUFFER_SIZE];
		    int longueur;
		    while ( ( longueur= entree.read( tampon ) ) > 0 ) {
		        sortie.write( tampon, 0, longueur );
		    }
		} finally {
		    try {
		        sortie.close();
		    } catch ( IOException ignore ) {
		    }
		    try {
		        entree.close();
		    } catch ( IOException ignore ) {
		    }
		}
	}
}
