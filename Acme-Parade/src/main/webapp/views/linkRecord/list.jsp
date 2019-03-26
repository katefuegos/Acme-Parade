<%--
 *
 * Copyright (C) 2017 Universidad de Sevilla
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

<display:table name="linkRecords" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="title" titleKey="linkRecord.title" />

	<display:column property="description"
		titleKey="linkRecord.description" />

	<display:column property="brotherhood.title"
		titleKey="linkRecord.brotherhood" />
		
		<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="linkRecord/brotherhood/edit.do?linkRecordId=${row.id}">
				<spring:message code="linkRecord.edit" />
			</a>
		</display:column>
	</security:authorize>

</display:table>



<security:authorize access="hasRole('BROTHERHOOD')">
	<div>
		<a href="linkRecord/brotherhood/create.do?historyId=${historyId}">
			<spring:message code="linkRecord.create" />
		</a>
	</div>
</security:authorize>



<br />

<security:authorize access="hasRole('BROTHERHOOD')">
	<input type=button name="back"
		value="<spring:message code="linkRecord.back" />"
		onclick="javascript: relativeRedir('history/brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<input type=button name="back"
		value="<spring:message code="linkRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('MEMBER')">
	<input type=button name="back"
		value="<spring:message code="linkRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('CHAPTER')">
	<input type=button name="back"
		value="<spring:message code="linkRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="isAnonymous()">
	<input type=button name="back"
		value="<spring:message code="linkRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>