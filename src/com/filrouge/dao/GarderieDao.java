package com.filrouge.dao;

import static com.filrouge.dao.DAOUtil.fermetureSilencieuse;
import static com.filrouge.dao.DAOUtil.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import com.filrouge.bean.Utilisateur;
import com.filrouge.bean.TicketGarderie;

public class GarderieDao {
	private DAOFactory daoFactory;
	
	public GarderieDao(DAOFactory daoFactory) {
		this.daoFactory = daoFactory; 
	}

	public TicketGarderie trouver(String enfant, String date, String horaire){
		final String SQL_SELECT = "SELECT * FROM Garderie WHERE beneficiaire = ? && date_utilisation = ? && horaire = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    TicketGarderie garderie = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, enfant, date, horaire );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            garderie = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return garderie;
	}

	public void creer(TicketGarderie ticketGarderie) {
		final String SQL_INSERT = "INSERT INTO fil_rouge.Garderie (acheteur, beneficiaire, date_achat, date_utilisation, horaire, prix_achat) Values (?, ?, ?, ?, ?, ?)";
    	String acheteur = ticketGarderie.getAcheteur();
		String beneficiaire = ticketGarderie.getBeneficiaire();
		String dateAchat = ticketGarderie.getDateAchat().toString();
		String dateUtilisation = ticketGarderie.getDateUtilisation().toString();
		String horaire = ticketGarderie.getHoraire();
		Double prixAchat = ticketGarderie.getPrixAchat();
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		
		try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, acheteur, beneficiaire, dateAchat, dateUtilisation, horaire, prixAchat );
	        int statut = preparedStatement.executeUpdate();
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	            ticketGarderie.setId( String.valueOf(valeursAutoGenerees.getLong( 1 )) );
	        } else {
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( valeursAutoGenerees, preparedStatement, connexion );
	    }
	}
	
	private static TicketGarderie map( ResultSet resultSet ) throws SQLException {
	    TicketGarderie garderie = new TicketGarderie();
	    garderie.setId(resultSet.getString("idgarderie"));
	    garderie.setAcheteur(resultSet.getString("acheteur"));
	    garderie.setBeneficiaire(resultSet.getString("beneficiaire"));
	    garderie.setDateAchat(LocalDate.parse(resultSet.getString("date_achat")));
	    garderie.setDateUtilisation(LocalDate.parse(resultSet.getString("date_utilisation")));
	    garderie.setPrixAchat(resultSet.getDouble("prix_achat"));
	    garderie.setHoraire(resultSet.getString("horaire"));
	    return garderie;
    }

	public ArrayList<TicketGarderie> parcourir(Utilisateur user) {
		ArrayList<TicketGarderie> garderie = new ArrayList<TicketGarderie>();
		LocalDate auj = new LocalDate ();
		String jour = auj.toString();
		final String SQL_SELECT = "SELECT * FROM Garderie WHERE acheteur = ? && date_utilisation >= '"+ jour +"' ORDER BY date_utilisation ASC, horaire DESC";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, user.getId());
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	            garderie.add(map( resultSet ));
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
		return garderie;
	}
	
	public ArrayList<String> trouverLesEnfants (LocalDate auj, String horaire) throws DAOException {
    	ArrayList<String> enfants = new ArrayList<String> ();
    	final String SQL_SELECT = "SELECT Enfant.nom, Enfant.prenom, Enfant.date_naissance FROM Garderie JOIN Enfant on Enfant.id = Garderie.beneficiaire where date_utilisation = ? && horaire = ? ORDER BY Enfant.nom, Enfant.prenom ";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    String date = auj.toString();
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, date, horaire);
	        resultSet = preparedStatement.executeQuery();
	        while ( resultSet.next() ) {
	        	int age = (Years.yearsBetween((new LocalDate (resultSet.getString("date_naissance"))), (new LocalDate()))).getYears();
	            enfants.add(resultSet.getString("prenom") + " " +resultSet.getString("nom") + " " + age + "ans");
	        }
	    } catch ( SQLException e ) {
	    	System.out.println("rate");
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return enfants;
    }
	
	public ArrayList<Map<String, Object>> envoiAnnuel (String idParent, String idEnfant) throws DAOException {
    	ArrayList<Map<String, Object>> ListeGarderie = new ArrayList<Map<String, Object>> ();
    	final String SQL_SELECT = "select garderie.prix_achat, garderie.horaire from garderie join enfant on enfant.id = garderie.beneficiaire where garderie.acheteur = ? && enfant.id = ? order by enfant.prenom, enfant.nom";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, idParent, idEnfant);
	        resultSet = preparedStatement.executeQuery();
	        while ( resultSet.next() ) {
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("prix", resultSet.getBigDecimal("prix_achat"));
	        	map.put("horaire", resultSet.getString("horaire"));
	        	ListeGarderie.add(map);
	        }
	    } catch ( SQLException e ) {
	    	System.out.println("rate");
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return ListeGarderie;
    }
	
}
