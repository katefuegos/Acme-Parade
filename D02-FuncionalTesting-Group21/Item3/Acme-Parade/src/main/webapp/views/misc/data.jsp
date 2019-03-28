<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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

<input type="button"
	value="<spring:message code="misc.exportPersonalData"/>"
	onclick="javascript: window.location.href = 'data/downloadPersonalData.do';" />

<input type="button"
	value="<spring:message code="misc.exportSocialProfiles"/>"
	onclick="javascript: window.location.href = 'data/downloadSocialProfiles.do';" />

<br>
<br>
<security:authorize access="hasRole('BROTHERHOOD')">
	<input type="button"
		value="<spring:message code="misc.deletePersonalData"/>"
		onclick="javascript: window.location.href = 'data/deletePersonalData.do';" />
	<br>
	<spring:message code="misc.deletePersonalDataInfo" />
</security:authorize>
<security:authorize access="hasRole('MEMBER')">
	<input type="button"
		value="<spring:message code="misc.deletePersonalData"/>"
		onclick="javascript: window.location.href = 'data/deletePersonalData.do';" />
	<br>
	<spring:message code="misc.deletePersonalDataInfo" />
</security:authorize>
<security:authorize access="hasRole('CHAPTER')">
	<input type="button"
		value="<spring:message code="misc.deletePersonalData"/>"
		onclick="javascript: window.location.href = 'data/deletePersonalData.do';" />
	<br>
	<spring:message code="misc.deletePersonalDataInfo" />
</security:authorize>