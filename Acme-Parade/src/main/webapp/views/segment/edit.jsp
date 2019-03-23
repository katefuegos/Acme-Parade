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

	<jstl:if test="${originLatitude == null}">
		<acme:textbox code="path.originLatitude" path="originLatitude" />
		<acme:textbox code="path.originLongitude" path="originLongitude" />
	</jstl:if>

	<jstl:if test="${originLatitude != null}">
		<form:label path="segment.originLatitude">
			<spring:message code="path.originLatitude" />:
		</form:label>
		<form:textarea path="segment.originLatitude" readonly="true" />
		<form:errors cssClass="error" path="segment.originLatitude" />

		<form:label path="segment.originLongitudee">
			<spring:message code="path.originLongitude" />:
		</form:label>
		<form:textarea path="segment.originLongitude" readonly="true" />
		<form:errors cssClass="error" path="segment.originLongitude" />
	</jstl:if>

	<acme:textbox code="path.approximateTimeOri" path="approximateTimeOri" />
	<acme:textbox code="path.destinationLatitude" path="approximateTimeOri" />
	<acme:textbox code="path.destinationLongitude"
		path="approximateTimeOri" />
	<acme:textbox code="path.approximateTimeDes" path="approximateTimeDes" />

</form:form>

<acme:submit name="save" code="segment.save" />
<acme:cancel url="/segment/brotherhood/list.do?pathId=${pathId}"
	code="segment.back" />