package com.filrouge.dao;

import static com.filrouge.dao.DAOUtil.fermetureSilencieuse;
import static com.filrouge.dao.DAOUtil.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.joda.time.LocalDate;

import com.filrouge.bean.Enfant;
import com.filrouge.bean.Utilisateur;

public class EnfantDao {
	private DAOFactory daoFactory;

    EnfantDao( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }
    
    public void creer (Enfant enfant) throws DAOException{
    	final String SQL_INSERT = "INSERT INTO fil_rouge.Enfant (nom, prenom, date_naissance, parent1, parent2) Values (?, ?, ?, ?, ?)";
    	String nom = enfant.getNom();
		String prenom = enfant.getPrenom();
		String anniversaire = enfant.getAnniversaire().toString();
		String parent1 = enfant.getParent1();
		String parent2 = enfant.getParent2();
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;
		
		try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, nom, prenom, anniversaire, parent1, parent2 );
	        int statut = preparedStatement.executeUpdate();
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	            enfant.setId( String.valueOf(valeursAutoGenerees.getLong( 1 )) );
	        } else {
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( valeursAutoGenerees, preparedStatement, connexion );
	    }
    }
    public Enfant trouver( String nom, String prenom ) throws DAOException {
    	final String SQL_SELECT = "SELECT * FROM Enfant WHERE nom = ? && prenom = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Enfant enfant = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, nom, prenom );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            enfant = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return enfant;
	}
    public Enfant trouver( String id ) throws DAOException {
    	final String SQL_SELECT = "SELECT * FROM Enfant WHERE id = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Enfant enfant = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, id );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            enfant = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return enfant;
	}
    
	private static Enfant map( ResultSet resultSet ) throws SQLException {
	    Enfant enfant = new Enfant();
	    enfant.setId(resultSet.getString("id"));
	    enfant.setNom(resultSet.getString("nom"));
	    enfant.setPrenom(resultSet.getString("prenom"));
	    enfant.setAnniversaire(LocalDate.parse(resultSet.getString("date_naissance")));
	    enfant.setParent1(resultSet.getString("parent1"));
	    enfant.setParent2(resultSet.getString("parent2"));
	    return enfant;
    }
    public void modifier( Enfant enfant ) throws DAOException {
    	final String SQL_INSERT = "UPDATE fil_rouge.Enfant SET date_naissance = ?, parent1 = ?, parent2 = ? WHERE nom = ? && prenom = ?";
    	String nom = enfant.getNom();
		String prenom = enfant.getPrenom();
		String anniversaire = enfant.getAnniversaire().toString();
		String parent1 = enfant.getParent1();
		String parent2 = enfant.getParent2();
		
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		
		try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, anniversaire, parent1, parent2, nom, prenom );
	        int statut = preparedStatement.executeUpdate();
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( preparedStatement, connexion );
	    }
	}
    void supprimer( Enfant enfant ) throws DAOException {
	}
    
    public ArrayList<Enfant> parcourir (Utilisateur parent) throws DAOException {
    	ArrayList<Enfant> enfants = new ArrayList<Enfant> ();
    	final String SQL_SELECT = "SELECT * FROM Enfant WHERE parent1 = ? || parent2 = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Enfant enfant = null;
	    
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, parent.getId(), parent.getId() );
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	            enfant = map( resultSet );
	            enfants.add(enfant);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return enfants;
    }
    public ArrayList<Enfant> parcourir () throws DAOException {
    	ArrayList<Enfant> enfants = new ArrayList<Enfant> ();
    	final String SQL_SELECT = "SELECT * FROM Enfant ORDER BY nom, prenom";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Enfant enfant = null;
	    
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false);
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	            enfant = map( resultSet );
	            enfants.add(enfant);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
    	return enfants;
    }
}
