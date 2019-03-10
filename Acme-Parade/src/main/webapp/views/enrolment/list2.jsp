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

<spring:message code="enrolment.accepted" />
<br>
<display:table name="enrolmentsAccepted" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="brotherhood.title"
		titleKey="enrolment.brotherhood.title" />

	<display:column property="momentEnrol"
		titleKey="enrolment.momentEnrol" />

	<display:column>
		<jstl:forEach var="entry" items="${row.position.name}">
			<jstl:if test="${lang==entry.key}">
				<jstl:out value="${entry.value}" />
			</jstl:if>
		</jstl:forEach>
	</display:column>

	<security:authorize access="hasRole('MEMBER')">
		<display:column>
			<jstl:if test="${row.accepted==true}">
				<a
					href="enrolment/member/dropout.do?enrolmentId=${row.id}">
					<spring:message code="enrolment.out" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>
<br>
<br>
<spring:message code="enrolment.pending" />
<br>
<display:table name="enrolmentsPending" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="brotherhood.title"
		titleKey="enrolment.brotherhood.title" />

	<security:authorize access="hasRole('MEMBER')">
		<display:column>
			<jstl:if test="${row.accepted==false}">
				<a href="enrolment/member/cancel.do?enrolmentId=${row.id}">
					<spring:message code="enrolment.cancel" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>
<br>
<br>
<spring:message code="enrolment.dropedOut" />
<br>
<display:table name="enrolmentsDropedOut" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="brotherhood.title"
		titleKey="enrolment.brotherhood.title" />

	<display:column property="momentDropOut"
		titleKey="enrolment.momentDropOut" />

</display:table>
<br>
<br>
<spring:message code="enrolment.brotherhoodsAvailable" />
<br>
<display:table name="brotherhoods" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="title" titleKey="brotherhood.title" />

	<display:column titleKey="brotherhood.members">
		<a
			href="member/list.do?brotherhoodId=${row.id}">
			<spring:message code="brotherhood.view" />
		</a>
	</display:column>
	
	<display:column titleKey="brotherhood.processions">
		<a
			href="procession/list.do?brotherhoodId=${row.id}">
			<spring:message code="brotherhood.view" />
		</a>
	</display:column>
	
	<display:column titleKey="brotherhood.floats">
		<a
			href="float/list.do?brotherhoodId=${row.id}">
			<spring:message code="brotherhood.view" />
		</a>
	</display:column>
	<display:column>
				<a href="enrolment/member/join.do?brotherhoodId=${row.id}">
					<spring:message code="enrolment.join" />
				</a>
		</display:column>
</display:table>