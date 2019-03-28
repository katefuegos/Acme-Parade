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

<display:table name="positions" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	<display:column>
		
			<a href="position/administrator/edit.do?positionId=${row.id}"> <spring:message
					code="position.edit" />
			</a>
	</display:column>

	<display:column titleKey="position.name">
		
		<jstl:forEach var="entry" items="${row.name}">
			<jstl:if test="${lang==entry.key}">
				<jstl:out value="${entry.value}" />
			</jstl:if>
		</jstl:forEach>

	</display:column>

</display:table>

<br />
<a href="position/administrator/create.do"> <spring:message
		code="position.create" />
</a>