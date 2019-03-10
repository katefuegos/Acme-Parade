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

<form:form action="area/edit.do" modelAttribute="area">

<form:hidden path="id" />
	<form:hidden path="version" />
	<acme:textbox code="area.name" path="name"/>
	<acme:textarea code="area.pictures" path="pictures"/>
	<acme:submit name="save" code="area.save"/>
		<jstl:if test="${area.id != 0}">
			<acme:delete confirmDelete="area.confirm.delete" name="delete" code="area.delete"/>
		</jstl:if>
		<acme:cancel url="area/list.do" code="area.cancel"/>

</form:form>
