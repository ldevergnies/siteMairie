package com.filrouge.dao;

import static com.filrouge.dao.DAOUtil.fermetureSilencieuse;
import static com.filrouge.dao.DAOUtil.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public class ConstantesDao {
	private DAOFactory daoFactory;

    ConstantesDao( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;        
    }
    
    public double trouver( String name ) throws DAOException {
    	final String SQL_SELECT = "SELECT value FROM constantes WHERE name = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    double resultat = 0;
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, name );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            resultat = resultSet.getDouble("value");
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    return resultat;
	}
    
    public void modifier( double value, String name ) throws DAOException {
    	final String SQL_SELECT = "UPDATE constantes SET value = ? WHERE name = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, value, name );
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
    
    public void modifier( LocalDateTime date, String name ) throws DAOException {
    	final String SQL_SELECT = "UPDATE constantes SET date = ? WHERE name = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    String mmt = date.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S"));
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, mmt, name );
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

	public LocalDateTime trouverHeure(String name) {
		final String SQL_SELECT = "SELECT date FROM constantes WHERE name = ?";
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    LocalDateTime resultat = null;
	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT, false, name );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            resultat = LocalDateTime.parse(resultSet.getString("date"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S"));
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    return resultat;
	}
}
    	
		