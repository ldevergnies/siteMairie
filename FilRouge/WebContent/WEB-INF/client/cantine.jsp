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
	<h3>Inscrivez votre enfant à la cantine</h3>
	<form method="post" action="cantine">
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
			<div class="no-scroll1">
				<label for="date">Dates (ctrl + clic pour sélectionner plusieurs dates)</label>
				<select id="date" name="date" class="no-scroll" multiple="multiple" size="5">
				<optgroup>
				<option disabled="disabled">Lundi</option>
				<option disabled="disabled">Mardi</option>
				<option disabled="disabled">Mercredi</option>
				<option disabled="disabled">Jeudi</option>
				<option disabled="disabled">Vendredi</option>
				</optgroup>
				<optgroup>
					<c:forEach items="${dates}" var="dat" begin="0" end="4">
						<option value="${dat}">
							<c:out value="${dat}" />
  						</option>
					</c:forEach>
				</optgroup>
				<optgroup>
					<c:forEach items="${dates}" var="dat" begin="5" end="9">
						<option value="${dat}">
							<c:out value="${dat}" />
  						</option>
					</c:forEach>
				</optgroup>
				<optgroup>
					<c:forEach items="${dates}" var="dat" begin="10" end="14">
						<option value="${dat}">
							<c:out value="${dat}" />
  						</option>
					</c:forEach>
				</optgroup>
				<optgroup>
					<c:forEach items="${dates}" var="dat" begin="15" end="19">
						<option value="${dat}">
							<c:out value="${dat}" />
  						</option>
					</c:forEach>
				</optgroup>
				</select>
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
</body>
</html>