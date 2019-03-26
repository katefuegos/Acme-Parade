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

<display:table name="miscellaneousRecords" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="title" titleKey="miscellaneousRecord.title" />
	<display:column property="description"
		titleKey="miscellaneousRecord.description" />


	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a
				href="miscellaneousRecord/brotherhood/edit.do?miscellaneousRecordId=${row.id}">
				<spring:message code="miscellaneousRecord.edit" />
			</a>
		</display:column>
	</security:authorize>

</display:table>



<security:authorize access="hasRole('BROTHERHOOD')">
	<div>
		<a
			href="miscellaneousRecord/brotherhood/create.do?historyId=${historyId}">
			<spring:message code="miscellaneousRecord.create" />
		</a>
	</div>
</security:authorize>

<br />

<security:authorize access="hasRole('BROTHERHOOD')">
	<input type=button name="back"
		value="<spring:message code="miscellaneousRecord.back" />"
		onclick="javascript: relativeRedir('history/brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<input type=button name="back"
		value="<spring:message code="miscellaneousRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('MEMBER')">
	<input type=button name="back"
		value="<spring:message code="miscellaneousRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('CHAPTER')">
	<input type=button name="back"
		value="<spring:message code="miscellaneousRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="isAnonymous()">
	<input type=button name="back"
		value="<spring:message code="miscellaneousRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>