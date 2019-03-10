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


<display:table name="floaats" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="title" titleKey="floaat.title" />

	<display:column property="description" titleKey="floaat.description" />

	<display:column property="pictures" titleKey="floaat.pictures" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<a href="float/brotherhood/display.do?floaatId=${row.id}"> <spring:message
					code="floaat.display"></spring:message></a>
		</display:column>

		<display:column>
			<a href="float/brotherhood/edit.do?floaatId=${row.id}"> <spring:message
					code="floaat.edit"></spring:message></a>
		</display:column>


	</security:authorize>


</display:table>
<br>
<security:authorize access="hasRole('BROTHERHOOD')">
	<a href="float/brotherhood/create.do"> <spring:message
			code="floaat.create" /></a>
</security:authorize>