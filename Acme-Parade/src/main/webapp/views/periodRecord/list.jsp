<%--
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="periodRecords" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="periodRecord/brotherhood/edit.do?periodRecordId=${row.id}">
			<spring:message code="periodRecord.edit" /></a>
		</display:column>
	</security:authorize>

	<display:column property="startYear" titleKey="periodRecord.startYear" />

	<display:column property="endYear" titleKey="periodRecord.endYear" />

	<display:column property="photos" titleKey="periodRecord.photos" />
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column titleKey="periodRecord.delete">
			<a href="periodRecord/brotherhood/delete.do?periodRecordId=${row.id}"><spring:message code="periodRecord.delete" /></a>
		</display:column>
	</security:authorize>

</display:table>



<security:authorize access="hasRole('BROTHERHOOD')">
	<div> <a href="periodRecord/brotherhood/create.do?historyId=${historyId}"> 
	<spring:message code="periodRecord.create" />
	</a>
	</div>
</security:authorize>



<br />

<input type=button name="back"
	value="<spring:message code="periodRecord.back" />"
	onclick="javascript: relativeRedir('history/brotherhood/list.do');" />