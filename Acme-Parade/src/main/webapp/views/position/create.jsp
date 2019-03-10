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

<form:form action="position/administrator/create.do"
	modelAttribute="positionForm">

	<form:hidden path="id" />
	
	<acme:textbox code="position.nameEN" path="nameEN"/>
	<acme:textbox code="position.nameES" path="nameES"/>

	<br />

	
	<acme:submit name="save" code="position.save"/>
	<acme:cancel url="position/administrator/list.do" code="position.cancel"/>

</form:form>