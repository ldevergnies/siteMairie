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
	<h3>Bonjour ${sessionScope.user.prenom} ${sessionScope.user.nom}</h3>
	<div class = "contener">
		<div class = "row">
			<form method="post" action="parent">
				<fieldset>
				<p>
					<label for="email">Adresse e-mail du parent</label>
					<input type="email" name ="email" id="email" required/>
					<input class="envoi" type="submit" value="Regarder"/>
					<span class="erreur">${erreurs['email']}</span>
					<span class="erreurForm">${resultat}</span>
				</p>
				</fieldset>
			</form>
		</div>
		<div class = "row">
			<div class="col1 list">
				<div><c:out value="Cantine"></c:out></div>
				<c:forEach items="${cantine}" var="cant">		
					<div><c:out value="${cant}"></c:out></div>
				</c:forEach>
			</div>
			<div class="col1 list">
				<div><c:out value="Garderie"></c:out></div>
				<c:forEach items="${garderiema}" var="gard">
					<div><c:out value="${gard}"></c:out></div>
				</c:forEach>
			</div>
			<div class="col1 list">
				<div><c:out value="Garderie"></c:out></div>
				<c:forEach items="${garderieam}" var="gard">
					<div><c:out value="${gard}"></c:out></div>
				</c:forEach>
			</div>
			<div class="col1 list">
				<div><c:out value="Activite"></c:out></div>
				<c:forEach items="${activite}" var="act">
					<div><c:out value="${act}"></c:out></div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
</html>