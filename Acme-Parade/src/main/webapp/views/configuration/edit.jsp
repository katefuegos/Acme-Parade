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

<form:form action="${requestURI}" modelAttribute="configurationForm">


	<img src="${configurationForm.bannerr}" height="250px" width="350px" />
	<br />
	<br />

	<acme:textbox code="configuration.bannerr" path="bannerr"/>
	<acme:textbox code="configuration.systemName" path="systemName"/>
	<acme:textbox code="configuration.countryCode" path="countryCode"/>
	<acme:textbox code="configuration.cache" path="finderCacheTime"/>
	<acme:textbox code="configuration.maxResults" path="finderMaxResults"/>
	<acme:textbox code="configuration.welcomeMessageES" path="welcomeMessageES"/>
	<acme:textbox code="configuration.welcomeMessageEN" path="welcomeMessageEN"/>
	<acme:textbox code="configuration.spamES" path="spamWordsES"/>
	<acme:textbox code="configuration.spamEN" path="spamWordsEN"/>
	<acme:textbox code="configuration.negativeEN" path="negativeWordsEN"/>
	<acme:textbox code="configuration.negativeES" path="negativeWordsES"/>
	<acme:textbox code="configuration.positiveES" path="positiveWordsES"/>
	<acme:textbox code="configuration.positiveEN" path="positiveWordsEN"/>
	

	<%-- 
	<form:label path="positionES">
		<spring:message code="configuration.positionES" />
	</form:label>
	<form:input path="positionES" readonly="${isRead}" />
	<form:errors cssClass="error" path="positionES" />
	<br />
	
	<form:label path="positionEN">
		<spring:message code="configuration.positionEN" />
	</form:label>
	<form:input path="positionEN" readonly="${isRead}" />
	<form:errors cssClass="error" path="positionEN" />
	<br />
 --%>
	<jstl:if test="${isRead == false}">
		<acme:submit name="save" code="configuration.save"/>
		<acme:cancel url="configuration/administrator/list.do" code="configuration.cancel"/>
	</jstl:if>

	<jstl:if test="${isRead == true}">
		<acme:cancel url="configuration/administrator/list.do" code="configuration.cancel"/>
	</jstl:if>
</form:form>
