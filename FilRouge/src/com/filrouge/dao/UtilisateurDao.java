package com.filrouge.dao;

import java.util.ArrayList;

import com.filrouge.bean.Utilisateur;

public interface UtilisateurDao {
	void creer( Utilisateur utilisateur ) throws DAOException;
    Utilisateur trouver( String email ) throws DAOException;
    void modifier( Utilisateur utilisateur ) throws DAOException;
    void supprimer( Utilisateur utilisateur ) throws DAOException;
	ArrayList<Utilisateur> parcourir();
}
