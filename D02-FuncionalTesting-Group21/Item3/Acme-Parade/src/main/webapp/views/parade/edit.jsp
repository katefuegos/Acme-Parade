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

<form:form action="${requestURI}" modelAttribute="paradeForm">
	<form:hidden path="id" />
	
	<acme:textbox code="parade.title" path="title"/>
	<acme:textbox code="parade.description" path="description"/>

	<form:label path="moment">
		<spring:message code="parade.moment" />:
	</form:label>
	<form:input path="moment" readonly="${isRead}"
		placeholder="yyyy/mm/dd HH:mm" />
	<form:errors cssClass="error" path="moment" />
	<br />

	<form:label path="draftMode">
		<spring:message code="parade.draftMode" />
	</form:label>
	<form:checkbox path="draftMode" />
	<form:errors path="draftMode" cssClass="error" />
	<br />


	<jstl:if test="${isRead == false}">
			<acme:submit name="save" code="parade.save"/>
		<jstl:if test="${id != 0}">
			<acme:delete confirmDelete="parade.confirmDelete" name="delete" code="parade.delete"/>
		
		</jstl:if>
		<acme:cancel url="parade/brotherhood/list.do" code="parade.cancel"/>
	</jstl:if>


	<jstl:if test="${isRead == true}">
		<acme:cancel url="parade/brotherhood/list.do" code="parade.cancel"/>

	</jstl:if>

</form:form>