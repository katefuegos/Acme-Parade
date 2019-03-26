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


<form:form action="history/brotherhood/create.do"
	modelAttribute="historyForm">

	<form:hidden path="id" />
	<form:hidden path="brotherhood" />

	<acme:textbox code="history.title" path="title" />
	<acme:textbox code="history.description" path="description" />
	<acme:textbox code="history.photos" path="startYear" />

	<acme:submit name="save" code="history.save" />
	<acme:cancel url="history/brotherhood/list.do"
		code="history.cancel" />
</form:form>