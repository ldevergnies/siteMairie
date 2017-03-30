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
<h3>Inscrivez votre enfant à la garderie</h3>
<div class = "contener">
	<form method="post" action="garderie">
		<fieldset>
			<p>
				<label for="enfant">Enfant</label>
				<select id="enfant" name="enfant">
					<c:forEach items="${enfants}" var="enf">
						<option value="${enf.id}">
							<c:out value="${enf.prenom}" />
      						<c:out value="${enf.nom}" />
  						</option>
					</c:forEach>
				</select>
				<span class="erreur">${erreurs['enfant']}</span>
			</p>
			<div class="no-scroll">
				<div class = "row">
					<div class = "col1">
					<p>Lundi</p>
					<c:if test="${dates[0].dayOfWeek > '1' }">
					<div class = "col"><br><br><br><br></div>
					</c:if>
					<c:forEach items="${dates}" var="dat">
						<c:if test= "${dat.dayOfWeek == '1'}">							
							<c:out value="${dat}" />
							<div class=col>							
							<input type="checkbox" name="${dat}" value="ma">matin<br>
							<input type="checkbox" name="${dat}" value = "am">après-midi<br><br>
							</div>
						</c:if>
					</c:forEach>
					</div>
					<div class = "col1">
					<p>Mardi</p>
					<c:if test="${dates[0].dayOfWeek > '2' }">
					<div class = "col"><br><br><br><br></div>
					</c:if>
					<c:forEach items="${dates}" var="dat">
						<c:if test= "${dat.dayOfWeek == '2'}">							
							<c:out value="${dat}" />
							<div class=col>							
							<input type="checkbox" name="${dat}" value="ma">matin<br>
							<input type="checkbox" name="${dat}" value = "am">après-midi<br><br>
							</div>
						</c:if>
					</c:forEach>
					</div>
					<div class = "col1">
					<p>Mercredi</p>
					<c:if test="${dates[0].dayOfWeek > '3' }">
					<div class = "col"><br><br><br><br></div>
					</c:if>
					<c:forEach items="${dates}" var="dat">
						<c:if test= "${dat.dayOfWeek == '3'}">
							<c:out value="${dat}" />
							<div class=col>
							<br>
							<input type="checkbox" name="${dat}" value = "am">après-midi<br><br>
							</div>
						</c:if>
					</c:forEach>
					</div>
					<div class = "col1">
					<p>Jeudi</p>
					<c:if test="${dates[0].dayOfWeek > '4' }">
					<div class = "col"><br><br><br><br></div>
					</c:if>
					<c:forEach items="${dates}" var="dat">
						<c:if test= "${dat.dayOfWeek == '4'}">
							<c:out value="${dat}" />
							<div class=col>
							<br>
							<input type="checkbox" name="${dat}" value = "am">après-midi<br><br>
							</div>
						</c:if>
					</c:forEach>
					</div>
					<div class = "col1">
					<p>Vendredi</p>
					<c:forEach items="${dates}" var="dat">
						<c:if test= "${dat.dayOfWeek == '5'}">
							<c:out value="${dat}" />
							<div class=col>
							<input type="checkbox" name="${dat}" value="ma">matin<br>
							<input type="checkbox" name="${dat}" value = "am">après-midi<br><br>
							</div>
						</c:if>
					</c:forEach>
					</div>
				</div>
				<span class="erreur">${erreurs['date']}</span>
			</div>
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