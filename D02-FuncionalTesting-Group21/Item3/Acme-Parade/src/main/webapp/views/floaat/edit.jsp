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

<form:form action="${requestURI}" modelAttribute="floaatForm">
	<form:hidden path="id" />
	

	<acme:textbox code="floaat.title" path="title"/>
	<acme:textbox code="floaat.description" path="description"/>
	<acme:textbox code="floaat.pictures" path="pictures"/>

	<jstl:if test="${isRead == false}">
			<acme:submit name="save" code="floaat.save"/>
		<jstl:if test="${id != 0}">
			<acme:delete confirmDelete="floaat.confirmDelete" name="delete" code="floaat.delete"/>
		</jstl:if>
		<acme:cancel url="float/brotherhood/list.do" code="floaat.cancel"/>
	</jstl:if>


	<jstl:if test="${isRead == true}">

		<acme:cancel url="/float/brotherhood/list.do" code="floaat.back"/>

	</jstl:if>

</form:form>