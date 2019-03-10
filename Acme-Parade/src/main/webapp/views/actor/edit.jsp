<%--
 * edit.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>




<form:form action="${requestURI}" modelAttribute="actorForm">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="userAccount" />
	<form:hidden path="auth" />
	<form:hidden path="area" />
	


	<jstl:if test="${isRead==true}">
		
	
		<img src="${actorForm.photo}" height="200px" width="200px" />
		<br />
	</jstl:if>

	<acme:textbox code="actor.name" path="name"/>
	<acme:textbox code="actor.middleName" path="middleName"/>
	<acme:textbox code="actor.surname" path="surname"/>

	<jstl:if test="${isRead == false}">
		<acme:textbox code="actor.photo" path="photo"/>
	</jstl:if>

	<acme:textbox code="actor.email" path="email"/>

	<acme:textbox code="actor.address" path="address"/>

	<jstl:if test="${isRead == true}">
		<jstl:if test="${title != null}">
			<h3>
				<spring:message code="actor.title" />
				:
				<jstl:out value="${title}" />
			</h3>
		</jstl:if>
	</jstl:if>


	<form:label path="phone">
		<spring:message code="actor.phone" />
	</form:label>
	<form:input path="phone" id="tlf" readonly="${isRead}" />
	<form:errors path="phone" cssClass="error" />
	<br />

	<script type="text/javascript">
		function isValid() {
			var phoneRe = /^(((\+[1-9][0-9]{0,2}) \(([1-9][0-9]{0,2})\) (\d\d\d\d+))|((\+[1-9][0-9]{0,2}) (\d\d\d\d+))|((\d\d\d\d+)))$/;
			var digits = document.getElementById('tlf').value;
			var res = phoneRe.test(digits);
			if (res) {
				return true;
			} else {
				return confirm('<spring:message code="phone.confirm" />');
			}
		}
	</script>

	<jstl:if test="${isRead == true}">
		<jstl:if test="${establishmentDate != null}">
			<h3>
				<spring:message code="actor.establishmentDate" />
				:
				<jstl:out value="${establishmentDate}" />
			</h3>
		</jstl:if>
	</jstl:if>

<jstl:if test="${actorForm.auth != 'BROTHERHOOD'}">
	<form:hidden path="title" />
	<form:hidden path="pictures" />
	
</jstl:if>
	<jstl:if test="${actorForm.auth == 'BROTHERHOOD'}">

		<acme:textbox code="actor.title" path="title"/>

		<acme:textbox code="actor.pictures" path="pictures"/>

		

	</jstl:if>


	<jstl:if test="${isRead == false}">
		<br />
		<acme:submit name="save" code="actor.save"/>

		<acme:cancel url="welcome/index.do" code="actor.back"/>
		<br />
	</jstl:if>

	<jstl:if test="${isRead == true}">
		<acme:cancel url="welcome/index.do" code="actor.back"/>

	</jstl:if>

</form:form>

