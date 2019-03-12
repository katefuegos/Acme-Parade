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


<display:table name="parades" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="ticker" titleKey="parade.ticker" />

	<display:column property="title" titleKey="parade.title" />

	<display:column property="moment" titleKey="parade.moment" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${row.draftMode == true}">
				<a href="parade/brotherhood/edit.do?paradeId=${row.id}">
					<spring:message code="parade.edit" />
				</a>
			</jstl:if>
		</display:column>
		<display:column>
				<a href="parade/brotherhood/show.do?paradeId=${row.id}">
					<spring:message code="parade.show" />
				</a>
		</display:column>
	</security:authorize>

</display:table>
<br>
<a href="parade/brotherhood/create.do"> <spring:message
		code="parade.create" />
</a>