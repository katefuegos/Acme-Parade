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

<form:form action="${requestURI}" modelAttribute="segmentForm">
	<form:hidden path="path" />
	<form:hidden path="id" />
	<form:hidden path="originLatitude" />
	<form:hidden path="originLongitude" />
	<form:hidden path="destinationLatitude" />
	<form:hidden path="destinationLongitude" />


	<acme:textbox code="path.approximateTimeOri" path="approximateTimeOri" />
	<acme:textbox code="path.approximateTimeDes" path="approximateTimeDes" />

	<acme:submit name="save" code="segment.save" />
	<acme:cancel url="/segment/brotherhood/list.do?pathId=${pathId}"
		code="segment.back" />

</form:form>

