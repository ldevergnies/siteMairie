<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="/FilRouge/css/filrouge.css" rel="stylesheet">
		<title>Insert title here</title>
	</head>
	<body>
	<%@ include file="/WEB-INF/header.jspf" %>
	<h3>Inscrivez votre enfant</h3>
	<div class = "contener">
		<form method="post" action="inscription_enfant">
			<fieldset>
				<p>
					<label for="nom">Nom</label>
					<input type="text" name ="nom" id="nom" required/>
					<span class="erreur">${erreurs['nom']}</span>
				</p>
				<p>
					<label for="prenom">Prénom</label>
					<input type="text" name ="prenom" id="prenom" required/>
					<span class="erreur">${erreurs['prenom']}</span>
				</p>
				<p>
					<label for="date">Date de naissance</label>
					<input type="text" name ="date" id="date" required/>
					<span class="erreur">${erreurs['date']}</span>
				</p>
			</fieldset>
			<c:if test="${user.access eq 'admin' }">
				<fieldset>
				<p>
					<label for="email">Adresse e-mail du parent</label>
					<input type="email" name ="email" id="email" required/>
					<span class="erreur">${erreurs['email']}</span>
				</p>
				</fieldset>
			</c:if>
			<p>
				<input class="envoi" type="submit" value="Inscrire"/>
				<span class="erreurForm">${resultat}</span>
			</p>
			
		</form>
	</div>
	</body>
</html>