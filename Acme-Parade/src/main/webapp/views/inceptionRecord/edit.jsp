€%<%--
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="inceptionRecord/brotherhood/edit.do" modelAttribute="inceptionRecordForm">
	<form:hidden path="id" />
	<form:hidden path="history" />
	
	<acme:textbox code="inceptionRecord.title" path="title"/>
	<acme:textbox code="inceptionRecord.description" path="description"/>
	<acme:textarea code="inceptionRecord.photos" path="photos"/>
	
	<acme:submit name="save" code="inceptionRecord.save"/>
	<acme:cancel url="history/brotherhood/list.do" code="inceptionRecord.cancel"/>
	<acme:delete confirmDelete="inceptionRecord.confirmDelete" name="delete" code="inceptionRecord.delete"/>
</form:form>