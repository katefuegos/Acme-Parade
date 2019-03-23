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

<h3>
	<spring:message code="parade" />
</h3>
<jstl:if test="${parades!=null}">
	<display:table name="parades" id="row" requestURI="${requestURI}"
		pagesize="5" class="displaytag">
		<display:column property="ticker" titleKey="parade.ticker" />
		<display:column property="title" titleKey="parade.title" />
		<display:column property="moment" titleKey="parade.moment" />
	</display:table>
</jstl:if>

<h3>
	<spring:message code="floaat.list" />
</h3>
<display:table name="floaatForm" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="title" titleKey="floaat.title" />

	<display:column property="description" titleKey="floaat.description" />

	<display:column property="pictures" titleKey="floaat.pictures" />

	<security:authorize access="hasRole('BROTHERHOOD')">
		<jstl:if test="${show!=true }">
		<display:column>
			<a href="float/brotherhood/display.do?floaatId=${row.id}"> <spring:message
					code="floaat.display"></spring:message></a>
		</display:column>
		
			<jstl:if test="${row.add==true }">
				<display:column>
					<a
						href="parade/brotherhood/removeFloat.do?floatId=${row.id}&paradeId=${row.paradeId}">
						<spring:message code="floaat.remove"></spring:message>
					</a>
				</display:column>
			</jstl:if>
			<jstl:if test="${row.add== false}">
				<display:column>
					<a
						href="parade/brotherhood/addFloat.do?floatId=${row.id}&paradeId=${row.paradeId}">
						<spring:message code="floaat.add"></spring:message>
					</a>
				</display:column>
			</jstl:if>
		</jstl:if>
	</security:authorize>


</display:table>
<br>
