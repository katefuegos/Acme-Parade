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


<display:table name="historys" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="title" titleKey="history.title" />

	<display:column property="description" titleKey="history.description" />

	<display:column property="photos" titleKey="history.photos" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="history/brotherhood/edit.do?historyId=${row.id}"> <spring:message
					code="history.edit" />
			</a>
		</display:column>
	</security:authorize>

</display:table>
<br>

<display:table name="historys" id="row" requestURI="${requestURI}"
	pagesize="4" class="displaytag">


	<!-- Realizar vistas de los records -->

	<display:column titleKey="history.periodRecords">
		<spring:url value="/periodRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<display:column titleKey="history.legalRecords">
		<spring:url value="/legalRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<display:column titleKey="history.linkRecords">
		<spring:url value="/linkRecord/brotherhood/list.do" var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

	<display:column titleKey="history.miscellaneousRecords">
		<spring:url value="/miscellaneousRecord/brotherhood/list.do"
			var="editURL">
			<spring:param name="historyId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="history.show" /> </a>
		<br />
	</display:column>

</display:table>

<br />

<br />

<security:authorize access="hasRole('BROTHERHOOD')">
	<jstl:if test="${vacio == true}">
		<input type="button" value="<spring:message code="history.create"/>"
			onclick="javascript: window.location.href = 'history/brotherhood/create.do';" />
	</jstl:if>
	<jstl:if test="${vacio == false}">
		<input type="button" value="<spring:message code="history.delete"/>"
			onclick="javascript: window.location.href = 'history/brotherhood/delete.do?historyId=${row.id}';" />
	</jstl:if>
</security:authorize>