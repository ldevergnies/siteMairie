<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="/FilRouge/css/jquerycssmenu.css" rel="stylesheet">
<link href="/FilRouge/css/filrouge.css" rel="stylesheet">
<title>Insert title here</title>
</head>
<body>
<%@ include file="/WEB-INF/header.jspf" %>
	<h3>Jouons avec les prix</h3>
	<div class = "contener">
		<form method="post" action="constantes">
			<fieldset>
				<p>
					<label for="cantine">Prix de la cantine</label>
					<input type="text" name ="cantine" id="cantine" required/>
					<span class="erreur">${erreurs['cantine']}</span>
				</p>
				<p>
					<label for="garderie">Prix de la garderie</label>
					<input type="text" name ="garderie" id="garderie" required/>
					<span class="erreur">${erreurs['garderie']}</span>
				</p>
				<p>
					<label for="foot">Prix du foot</label>
					<input type="text" name ="foot" id="foot" required/>
					<span class="erreur">${erreurs['foot']}</span>
				</p>
				<p>
					<label for="couture">Prix de la couture</label>
					<input type="text" name ="couture" id="couture" required/>
					<span class="erreur">${erreurs['couture']}</span>
				</p>
			</fieldset>
			<p>
				<input class="envoi" type="submit" value="Allez ! On explose les tarifs !"/>
				<span class="erreurForm">${erreurs['vide']} ${resultat}</span>
			</p>
		</form>
	</div>
</body>
</html>