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
	<h3>${parent.prenom} ${parent.nom}</h3>
	<div class = "contener">
		<div class="row1">
			<div class= "col">
				<p> adresse : <c:out value="${parent.adresse}" /> <br>
				code postal : <c:out value="${parent.codePostal}" /><br>
				ville : <c:out value="${parent.ville}" /><br>
				e-mail : <c:out value="${parent.adresseEmail}" /><br>
				telephone : <c:out value="${parent.numeroTelephone}" /> </p>
			</div>
			<div class= "col">
				<p> Vos enfants : </p>
				<p>
				<c:forEach items="${enfants}" var="enf">
						
							<c:out value="${enf.prenom}" />
      						<c:out value="${enf.nom}" />
  						<br>
				</c:forEach>
				</p>
			</div>
		</div>
		<div class="row">
			<div class= "col">
				<p> Vos tickets de cantine : </p>
				<p>
				<c:forEach items="${cantines}" var="cant">
						
							<c:out value="${cant.enfant}" />
							 le : 
      						<c:out value="${cant.ticket}" />
  						<br>
				</c:forEach>
				</p>
			</div>
			<div class= "col">
				<p> Vos tickets de garderie : </p>
				<p>
				<c:forEach items="${garderie}" var="gard">
						
							<c:out value="${gard.enfant}" />
							 le : 
      						<c:out value="${gard.ticket}" />
  						<br>
				</c:forEach>
				</p>
			</div>
			<div class= "col">
				<p> Vos tickets d'activite : </p>
				<p>
				<c:forEach items="${activite}" var="act">
						
							<c:out value="${act.enfant}" />
							 le : 
      						<c:out value="${act.ticket}" />
  						<br>
				</c:forEach>
				</p>
			</div>
		</div>
	</div>
</body>
</html>