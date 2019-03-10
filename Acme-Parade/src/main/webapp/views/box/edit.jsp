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

<form:form action="box/actor/edit.do" modelAttribute="box">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="subboxes" />
	<form:hidden path="actor" />

<acme:textbox code="box.name" path="name"/>
	

	<jstl:choose>
		<jstl:when test="${box.id == 0}">
			<form:label path="rootbox">
				<spring:message code="box.rootbox" />:
			</form:label>
			<form:select id="boxes" path="rootbox">
				<form:option value="" label="------" />
				<form:options items="${boxes}" itemValue="id" itemLabel="name" />
			</form:select>
			<form:errors cssClass="error" path="rootbox" />
			<br />
		</jstl:when>
		<jstl:otherwise>
			<form:hidden path="rootbox" />
			<spring:message code="box.rootbox" />:
				<jstl:out value="${box.rootbox.name}" />
			<br />
		</jstl:otherwise>
	</jstl:choose>

 
	<acme:submit name="save" code="box.save"/>
	
	<jstl:if test="${box.id != 0}">
	<acme:delete confirmDelete="box.confirm.delete" name="delete" code="box.delete"/>
	</jstl:if>

	<acme:cancel url="box/actor/list.do" code="box.cancel"/>
	<br />
</form:form>
