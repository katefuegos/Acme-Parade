<%--
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="linkRecord/brotherhood/create.do"
	modelAttribute="linkRecordForm">

	<form:hidden path="id" />
	<form:hidden path="history" />

	<acme:textbox code="linkRecord.title" path="title" />
	<acme:textbox code="linkRecord.description" path="description" />
	<spring:message code="linkRecord.brotherhood" />
	<form:select id="brotherhoods" path="brotherhood">
		<form:options items="${brotherhoods}" itemValue="id"
			itemLabel="userAccount.username" />
	</form:select>
	<form:errors cssClass="error" path="brotherhood" />
	<br>
	<acme:submit name="save" code="linkRecord.save" />
	<acme:cancel url="history/brotherhood/list.do" code="linkRecord.cancel" />
</form:form>