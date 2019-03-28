<%--
 * action-1.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="${action}" modelAttribute="messageForm">

	<form:hidden path="message.id" />
	<form:hidden path="message.version" />
	<form:hidden path="message.moment" />
	<form:hidden path="message.sender" />
	<form:hidden path="message.recipient" />
	<form:hidden path="message.box" />


	<form:label path="message.subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:textarea path="message.subject" readonly="${isRead}" />
	<form:errors cssClass="error" path="message.subject" />
	<br />
	<form:label path="message.body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="message.body" readonly="${isRead}" />
	<form:errors cssClass="error" path="message.body" />
	<br />
	<form:label path="message.tags">
		<spring:message code="message.tags" />:
	</form:label>
	<form:textarea path="message.tags" readonly="${isRead}" />
	<form:errors cssClass="error" path="message.tags" />
	<br />

	<form:label path="message.priority">
		<spring:message code="message.priority" />:
	</form:label>
	<form:select id="priorities" path="message.priority">
		<form:options items="${priorities}" />
	</form:select>
	<form:errors cssClass="error" path="message.priority" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="message.save" />" />&nbsp; 

	<input type="button" name="cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('welcome/index.do');" />
	<br />

</form:form>
