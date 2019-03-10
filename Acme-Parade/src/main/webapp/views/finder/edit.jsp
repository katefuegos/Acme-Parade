
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

<form:form action="finder/member/update.do" modelAttribute="finder">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="lastUpdate" />
	<form:hidden path="processions" />


	<acme:textbox code="finder.keyword" path="keyword"/>
	<acme:textbox code="finder.dateMin" path="dateMin"/>
	<acme:textbox code="finder.dateMax" path="dateMax"/>

	<form:label path="nameArea">
		<spring:message code="finder.nameArea" />
	</form:label>
	<form:select id="areas" path="nameArea">
		<form:option value="" label="------" />
		<form:options items="${areas}" />
	</form:select>


	<acme:submit name="save" code="finder.save"/>
	<acme:cancel url="finder/member/listProcessions.do" code="finder.cancel"/>


</form:form>