package com.filrouge.dao;

import static com.filrouge.dao.DAOUtil.fermetureSilencieuse;
import static com.filrouge.dao.DAOUtil.initialisationRequetePreparee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import com.filrouge.bean.Utilisateur;
import com.filrouge.bean.TicketCantine;

public class CantineDao {
	private DAOFactory daoFactory;

    CantineDao( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;        
    }
    
    public void creer (TicketCantine cantine) throws DAOException{
    	final String SQL_INSERT = "INSERT INTO fil_rouge.Cantine (acheteur, beneficiaire, date_achat, date_utilisation, prix_achat) Values (?, ?, ?, ?, ?)";
    	String acheteur = cantine.getAcheteur();
		String beneficiaire = cantine.getBeneficiaire();
		String dateAchat = cantine.getDateAchat().toString();
		String dateUtilisation = cantine.getDateUtilisation().toString();
		Double prixAchat = cantine.getPrixAchat();
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		
		try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, acheteur, beneficiaire, dateAchat, dateUtilisation, prixAchat );
	        int statut = preparedStatement.executeUpdate();
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	            cantine.setId( String.valueOf(valeursAutoGenerees.getLong( 1 )) );
	        } else {
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( valeursAutoGenerees, preparedStatement, connexion );
	    }
    }
    
    public TicketCantine trouver( String enfant, String date ) throws DAOException {
    	final String SQL_SELECT = "SELECT * FROM Cantine WHERE beneficiaire = ? && date_utilisation = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    TicketCantine cantine = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, enfant, date );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            cantine = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return cantine;
	}
    
    private static TicketCantine map( ResultSet resultSet ) throws SQLException {
	    TicketCantine cantine = new TicketCantine();
	    cantine.setId(resultSet.getString("idcantine"));
	    cantine.setAcheteur(resultSet.getString("acheteur"));
	    cantine.setBeneficiaire(resultSet.getString("beneficiaire"));
	    cantine.setDateAchat(LocalDate.parse(resultSet.getString("date_achat")));
	    cantine.setDateUtilisation(LocalDate.parse(resultSet.getString("date_utilisation")));
	    cantine.setPrixAchat(resultSet.getDouble("prix_achat"));
	    return cantine;
    }
    
    public ArrayList<TicketCantine> parcourir (Utilisateur parent) throws DAOException {
    	ArrayList<TicketCantine> cantines = new ArrayList<TicketCantine> ();
    	LocalDate auj = new LocalDate ();
		String jour = auj.toString();
    	String SQL_SELECT = "SELECT * FROM Cantine WHERE acheteur = ? && date_utilisation >= '"+ jour +"' ORDER BY date_utilisation";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, parent.getId());
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	            cantines.add(map( resultSet ));
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return cantines;
    }
    
    public ArrayList<String> trouverLesEnfants (LocalDate auj) throws DAOException {
    	ArrayList<String> enfants = new ArrayList<String> ();
    	final String SQL_SELECT = "SELECT Enfant.nom, Enfant.prenom, Enfant.date_naissance FROM Cantine JOIN Enfant on Enfant.id = Cantine.beneficiaire where date_utilisation = ? ORDER BY Enfant.nom, Enfant.prenom ";
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
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return enfants;
    }
    
    public ArrayList<BigDecimal> envoiAnnuel (String idParent, String idEnfant) throws DAOException {
    	ArrayList<BigDecimal> ListeCantine = new ArrayList<BigDecimal> ();
    	final String SQL_SELECT = "select cantine.prix_achat from cantine join enfant on enfant.id = cantine.beneficiaire where cantine.acheteur = ? && enfant.id = ? order by enfant.prenom, enfant.nom";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, idParent, idEnfant);
	        resultSet = preparedStatement.executeQuery();
	        while ( resultSet.next() ) {
	        	ListeCantine.add(resultSet.getBigDecimal("prix_achat"));
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return ListeCantine;
    }
}
