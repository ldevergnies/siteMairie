<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<link href="css/filrouge.css" rel="stylesheet">
	</head>
	<body>
		<%@ include file="/WEB-INF/header.jspf" %>
		<h3>Inscrivez-vous aux services municipaux</h3>
		<div class = "contener">
		
		<form method="post" action="inscription">
		
			<fieldset>
			<p>
				<label for="nom">Votre nom</label>
				<input type="text" name ="nom" id="nom" required/>
				<span class="erreur">${erreurs['nom']}</span>
			</p>
			<p>
				<label for="prenom">Votre prénom</label>
				<input type="text" name ="prenom" id="prenom" required/>
				<span class="erreur">${erreurs['prenom']}</span>
			</p>
			</fieldset>
			<p/>
			<fieldset>
			<p>
				<label for="adresse">Votre adresse</label>
				<input type="text" name="adresse" id="adresse" required/>
				<span class="erreur">${erreurs['adresse']}</span>
			</p>
			<p>
				<label for="codePostal">Votre code postal</label>
				<input type="number" name="codePostal" id="codePostal" value="59000" required/>
				<span class="erreur">${erreurs['codePostal']}</span>
			</p>
			<p>
				<label for="ville">Votre ville</label>
				<input type="text" name="ville" id="ville" value="Filrouge" required/>
				<span class="erreur">${erreurs['ville']}</span>
			</p>
			<p>
				<label for="adresseEmail">Votre adresse email</label>
				<input type="email" name="adresseEmail" id="adresseEmail" required/>
				<span class="erreur">${erreurs['adresseEmail']}</span>
			</p>
			<p>
				<label for="telephone">Votre numéro de téléphone</label>
				<input type="tel" name="telephone" id="telephone"/>
				<span class="erreur">${erreurs['telephone']}</span>
			</p>
			</fieldset>
			<p/>
			<fieldset>
			<p>
				<label for="password">Votre mot de passe</label>
				<input type="password" name="password" id="password" required/>
				<span class="erreur">${erreurs['password']}</span>
			</p>
			<p>
				<label for="password2">Validation du mot de passe</label>
				<input type="password" name="password2" id="password2" required/>
				<span class="erreur">${erreurs['password2']}</span>
			</p>
			</fieldset>
			<c:if test="${user.access eq 'admin' }">
				<fieldset>
				<p>
					<label for="accessLevel">Niveau d'access</label>
					<select id="accessLevel" name="accessLevel">
						<option value="parent">parent</option>
						<option value="cantine">cantine</option>
						<option value="garderie">garderie</option>
						<option value="activite">activite</option>
						<option value="admin">admin</option>
					</select>
					<span class="erreur">${erreurs['access']}</span>
				</p>
				</fieldset>
			</c:if>
			<p>
				<input type="submit" value="Inscrire"/>
				<span class="erreurForm">${resultat}</span>
			</p>
		</form>
		</div>
	</body>
</html>