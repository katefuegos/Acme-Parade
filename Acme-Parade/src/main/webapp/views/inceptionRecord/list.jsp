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

<display:table name="inceptionRecords" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a
				href="inceptionRecord/brotherhood/edit.do?inceptionRecordId=${row.id}">
				<spring:message code="inceptionRecord.edit" />
			</a>
		</display:column>
	</security:authorize>

	<display:column property="title" titleKey="inceptionRecord.title" />

	<display:column property="description"
		titleKey="inceptionRecord.description" />

	<display:column property="photos" titleKey="inceptionRecord.photos" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column titleKey="inceptionRecord.delete">
			<a
				href="inceptionRecord/brotherhood/delete.do?inceptionRecordId=${row.id}"><spring:message
					code="inceptionRecord.delete" /></a>
		</display:column>
	</security:authorize>

</display:table>



<security:authorize access="hasRole('BROTHERHOOD')">
	<div>
		<a href="inceptionRecord/brotherhood/create.do?historyId=${historyId}">
			<spring:message code="inceptionRecord.create" />
		</a>
	</div>
</security:authorize>



<br />

<security:authorize access="hasRole('BROTHERHOOD')">
	<input type=button name="back"
		value="<spring:message code="inceptionRecord.back" />"
		onclick="javascript: relativeRedir('history/brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
	<input type=button name="back"
		value="<spring:message code="inceptionRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('MEMBER')">
	<input type=button name="back"
		value="<spring:message code="inceptionRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="hasRole('CHAPTER')">
	<input type=button name="back"
		value="<spring:message code="inceptionRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>

<security:authorize access="isAnonymous()">
	<input type=button name="back"
		value="<spring:message code="inceptionRecord.back" />"
		onclick="javascript: relativeRedir('brotherhood/list.do');" />
</security:authorize>