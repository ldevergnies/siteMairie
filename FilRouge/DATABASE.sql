CREATE DATABASE fil_rouge CHARSET=utf8;
CREATE USER 'java'@'localhost' IDENTIFIED BY 'filrouge';
GRANT SELECT, UPDATE, DELETE, INSERT ON fil_rouge.* TO 'java'@'localhost';
USE fil_rouge;
CREATE TABLE Utilisateur (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	nom VARCHAR(25) NOT NULL,
	prenom VARCHAR(25) NOT NULL,
	adresse VARCHAR(25) NOT NULL,
	ville VARCHAR(25) NOT NULL,
	code_postal CHAR(5) NOT NULL,
	email VARCHAR(50) NOT NULL UNIQUE,
	telephone CHAR(14) NOT NULL,
	password VARCHAR(25) NOT NULL,
	access VARCHAR(25) NOT NULL,
	PRIMARY KEY (id)) ENGINE = InnoDB;
CREATE TABLE Enfant (
	id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	nom VARCHAR(25) NOT NULL,
	prenom VARCHAR(25) NOT NULL,
	date_naissance DATE NOT NULL,
	parent1 INT UNSIGNED NOT NULL,
	parent2 INT UNSIGNED,
	PRIMARY KEY (id)) ENGINE = InnoDB;
CREATE TABLE Garderie (
	idgarderie INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	acheteur INT UNSIGNED NOT NULL,
	beneficiaire INT UNSIGNED NOT NULL,
	date_achat DATE NOT NULL,
	date_utilisation DATE NOT NULL,
	horaire VARCHAR(25) NOT NULL,
	prix_achat INT NOT NULL,
	PRIMARY KEY (id)) ENGINE = InnoDB;
CREATE TABLE Cantine (
	idcantine INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	acheteur INT UNSIGNED NOT NULL,
	beneficiaire INT UNSIGNED NOT NULL,
	date_achat DATE NOT NULL,
	date_utilisation DATE NOT NULL,
	prix_achat INT NOT NULL,
	PRIMARY KEY (id)) ENGINE = InnoDB;
CREATE TABLE Activite (
	idactivite INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
	acheteur INT UNSIGNED NOT NULL,
	beneficiaire INT UNSIGNED NOT NULL,
	date_achat DATE NOT NULL,
	date_utilisation DATE NOT NULL,
	horaire VARCHAR(25) NOT NULL,
	prix_achat INT NOT NULL,
	type VARCHAR(25) NOT NULL,
	PRIMARY KEY (id)) ENGINE = InnoDB;
CREATE TABLE Constantes (
	name VARCHAR(25) NOT NULL UNIQUE,
	value DOUBLE,
	date DATETIME,
	PRIMARY KEY (name)) ENGINE = InnoDB;
INSERT INTO Utilisateur (nom, prenom, adresse, ville, code_postal, email, telephone, password, access) VALUES ('Administrateur', '', '', '', '', 'admin@mairie.fr', '', 'admin', 'admin');
INSERT INTO Constantes (name, value) VALUES ('prix_cantine', '0.0'), ('prix_garderie', '0.0'), ('prix_foot', '0.0'), ('prix_couture', '0.0');
INSERT INTO Constantes (name, date) VALUES ('date_year', '2017-06-01');