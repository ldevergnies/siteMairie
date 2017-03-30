package com.filrouge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.filrouge.dao.DAOUtil.*;

import com.filrouge.bean.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT * FROM Utilisateur WHERE email = ?";
	private static final String SQL_INSERT = "INSERT INTO fil_rouge.Utilisateur (nom, prenom, adresse, ville, code_postal, email, telephone, password, access) Values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private DAOFactory daoFactory;

    UtilisateurDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

	public void creer(Utilisateur utilisateur) throws DAOException {
		Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet valeursAutoGenerees = null;
	    
	    String nom = utilisateur.getNom();
		String prenom = utilisateur.getPrenom();
		String adresse = utilisateur.getAdresse();
		String ville = utilisateur.getVille();
		String codePostal = utilisateur.getCodePostal();
		String email = utilisateur.getAdresseEmail();
		String telephone = utilisateur.getNumeroTelephone();
		String password = utilisateur.getPassword();
		String access = utilisateur.getAccess();

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, nom, prenom, adresse, ville, codePostal, email, telephone, password, access );
	        int statut = preparedStatement.executeUpdate();
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	            utilisateur.setId( String.valueOf(valeursAutoGenerees.getLong( 1 )) );
	        } else {
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( valeursAutoGenerees, preparedStatement, connexion );
	    }
	}

	public Utilisateur trouver(String email) throws DAOException {
		Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Utilisateur utilisateur = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
	        resultSet = preparedStatement.executeQuery();
	        
	        if ( resultSet.next() ) {
	            utilisateur = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return utilisateur;
	}

	public void modifier(Utilisateur utilisateur) throws DAOException {
	}

	public void supprimer(Utilisateur utilisateur) throws DAOException {
	}
	
	private static Utilisateur map( ResultSet resultSet ) throws SQLException {
	    Utilisateur utilisateur = new Utilisateur();
	    utilisateur.setId(resultSet.getString("id"));
	    utilisateur.setNom(resultSet.getString("nom"));
	    utilisateur.setPrenom(resultSet.getString("prenom"));
	    utilisateur.setAdresse(resultSet.getString("adresse"));
	    utilisateur.setVille(resultSet.getString("ville"));
	    utilisateur.setCodePostal(resultSet.getString("code_postal"));
	    utilisateur.setAdresseEmail(resultSet.getString("email"));
	    utilisateur.setNumeroTelephone(resultSet.getString("telephone"));
	    utilisateur.setPassword(resultSet.getString("password"));
	    utilisateur.setAccess(resultSet.getString("access"));
	    return utilisateur;
	}
	
	public ArrayList<Utilisateur> parcourir() throws DAOException {
		ArrayList<Utilisateur> par = new ArrayList<Utilisateur>();
		Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Utilisateur utilisateur = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, "SELECT * FROM Utilisateur", false );
	        resultSet = preparedStatement.executeQuery();
	        
	        while ( resultSet.next() ) {
	            utilisateur = map( resultSet );
	            par.add(utilisateur);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermetureSilencieuse( resultSet, preparedStatement, connexion );
	    }
	    
	    return par;
	}
	
}