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
import com.filrouge.bean.TicketActivite;

public class ActiviteDao {
	private DAOFactory daoFactory;

    ActiviteDao( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;        
    }

    public TicketActivite trouver(String enfant, String date, String horaire){
		final String SQL_SELECT = "SELECT * FROM Activite WHERE beneficiaire = ? && date_utilisation = ? && horaire = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    TicketActivite activite = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, enfant, date, horaire );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            activite = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return activite;
	}
    
    private static TicketActivite map( ResultSet resultSet ) throws SQLException {
	    TicketActivite activite = new TicketActivite();
	    activite.setId(resultSet.getString("idactivite"));
	    activite.setAcheteur(resultSet.getString("acheteur"));
	    activite.setBeneficiaire(resultSet.getString("beneficiaire"));
	    activite.setDateAchat(LocalDate.parse(resultSet.getString("date_achat")));
	    activite.setDateUtilisation(LocalDate.parse(resultSet.getString("date_utilisation")));
	    activite.setPrixAchat(resultSet.getDouble("prix_achat"));
	    activite.setHoraire(resultSet.getString("horaire"));
	    activite.setType(resultSet.getString("type"));
	    return activite;
    }
    
    public void creer(TicketActivite activite) {
		final String SQL_INSERT = "INSERT INTO fil_rouge.activite (acheteur, beneficiaire, date_achat, date_utilisation, prix_achat, horaire, type) Values (?, ?, ?, ?, ?, ?, ?)";
    	String acheteur = activite.getAcheteur();
		String beneficiaire = activite.getBeneficiaire();
		String dateAchat = activite.getDateAchat().toString();
		String dateUtilisation = activite.getDateUtilisation().toString();
		String horaire = activite.getHoraire();
		Double prixAchat = activite.getPrixAchat();
		String type = activite.getType();
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		
		try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, acheteur, beneficiaire, dateAchat, dateUtilisation, prixAchat, horaire, type);
	        int statut = preparedStatement.executeUpdate();
	        if ( statut == 0 ) {
	        	System.out.println("oups");
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	        	System.out.println("cool");
	            activite.setId( String.valueOf(valeursAutoGenerees.getLong( 1 )) );
	        } else {
	        	System.out.println("oups1");
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	    	System.out.println("oups2");
	        throw new DAOException( e );
	    } finally {
	    	System.out.println("ok finally");
	        fermetureSilencieuse( valeursAutoGenerees, preparedStatement, connexion );
	    }
	}

	public ArrayList<String> trouverLesEnfants(LocalDate auj) {
		ArrayList<String> enfants = new ArrayList<String> ();
    	final String SQL_SELECT = "SELECT Enfant.nom, Enfant.prenom, Enfant.date_naissance FROM Activite JOIN Enfant on Enfant.id = Activite.beneficiaire where date_utilisation = ? ORDER BY Activite.type, Enfant.nom, Enfant.prenom ";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    String date = auj.toString();
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, date);
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
	
	public ArrayList<TicketActivite> parcourir(Utilisateur user) {
		ArrayList<TicketActivite> activite = new ArrayList<TicketActivite>();
		LocalDate auj = new LocalDate ();
		String jour = auj.toString();
		final String SQL_SELECT = "SELECT * FROM Activite WHERE acheteur = ? && date_utilisation >= '"+ jour +"' ORDER BY date_utilisation ASC, type";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, user.getId());
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	        	activite.add(map( resultSet ));
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
		return activite;
	}
	
	public ArrayList<Map<String, Object>> envoiAnnuel (String idParent, String idEnfant) throws DAOException {
    	ArrayList<Map<String, Object>> ListeActivite = new ArrayList<Map<String, Object>> ();
    	final String SQL_SELECT = "select activite.prix_achat, activite.type from activite join enfant on enfant.id = activite.beneficiaire where activite.acheteur = ? && enfant.id = ? order by enfant.prenom, enfant.nom";
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
	        	map.put("type", resultSet.getString("type"));
	        	ListeActivite.add(map);
	        }
	    } catch ( SQLException e ) {
	    	System.out.println("rate");
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return ListeActivite;
    }
}
