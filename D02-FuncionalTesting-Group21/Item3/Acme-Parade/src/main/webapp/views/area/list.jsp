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


<display:table name="areas" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="name" titleKey="area.name" />
	
	
	<display:column titleKey="area.pictures">
		<img src="${row.pictures}" height="100px" width="100px" />
	</display:column>
	
	<display:column >
			<a href="area/edit.do?areaId=${row.id}">
			<spring:message code="area.edit"></spring:message></a>
	</display:column>
	
	
	</display:table>
	
<a href="area/create.do"> <spring:message code="area.create" /></a>
	