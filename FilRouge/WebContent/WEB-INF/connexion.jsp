<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	<link href="css/filrouge.css" rel="stylesheet">
	</head>
	<body>
		<%@ include file="/WEB-INF/header.jspf" %>
		<h3>Connectez-vous aux services municipaux</h3>
		<div class="contener">
		<form method="post" action="<c:url value="connexion" />">
			<fieldset>
				<p>
					<label for="adresseEmail">Votre adresse email</label>
					<input type="email" name="adresseEmail" id="adresseEmail" required/>
					<span class="erreur">${erreurs['adresseEmail']}</span>
				</p>
				<p>
					<label for="password">Votre mot de passe</label>
					<input type="password" name="password" id="password" required/>
					<span class="erreur">${erreurs['password']}</span>
				</p>
			</fieldset>
			<p>
				<input type="submit" value="Connexion"/>
			</p>
		</form>
		</div>
	</body>
</html>