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
	<h3>Bienvenue ${sessionScope.user.prenom} ${sessionScope.user.nom}</h3>
	<div class = "contener">
		<div class = "row">
			<c:forEach items="${sem1}" var="jour">
				<div class="col list">
					<p>
					<c:forEach items="${jour}" var="enf">
						<c:out value="${enf}"></c:out>
						<br>
					</c:forEach>
					</p>
				</div>
			</c:forEach>
		</div>
		<div class = "row">
			<c:forEach items="${sem2}" var="jour">
				<div class="col list">
					<p>
					<c:forEach items="${jour}" var="enf">
						<c:out value="${enf}"></c:out>
						<br>
					</c:forEach>
					</p>
				</div>
			</c:forEach>
		</div>
		<div class = "row">
			<form action = "<c:out value="${dest}"></c:out>">
				<input class="envoi" type="submit" value="Télécharger le document"/>
			</form>
		</div>
	</div>
	
</body>
</html>