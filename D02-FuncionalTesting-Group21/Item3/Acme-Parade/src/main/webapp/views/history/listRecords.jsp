

<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="history" id="row" requestURI="${requestURI}" pagesize="4" class="displaytag">

	<!-- EDITAR -->

	<display:column titleKey="history.personalRecord">
		<spring:url value="/personalRecord/brotherhood/edit.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.edit" /> </a>
	</display:column>


	<!-- DETALLES -->

	<display:column titleKey="history.periodRecord">
		<spring:url value="/periodRecord/brotherhood/show.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
	</display:column>

	<!-- Realizar vistas de los records -->

	<display:column titleKey="history.periodRecords">
		<spring:url value="/periodRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<display:column titleKey="history.legalRecords">
		<spring:url value="/legalRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<display:column titleKey="history.linkRecords">
		<spring:url value="/linkRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<display:column titleKey="history.miscellaneousRecords">
		<spring:url value="/miscellaneousRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>
	
	<display:column titleKey="history.inceptionRecords">
		<spring:url value="/inceptionRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<!-- BORRAR -->

	<display:column>
		<input type="button" value="<spring:message code="history.delete"/>" 
		onclick="javascript:confirm('<spring:message code="history.confirm.delete" />');
			window.location.href='history/brotherhood/delete.do?historyId=${row.id}' ;" />
	</display:column>
	
</display:table>

<br />

<br />

<!-- CREAR -->

<security:authorize access="hasRole('BROTHERHOOD')">
	<input type="button" value="<spring:message code="history.create"/>"
		onclick="javascript: window.location.href = 'periodRecord/brotherhood/create.do';" />
</security:authorize>
