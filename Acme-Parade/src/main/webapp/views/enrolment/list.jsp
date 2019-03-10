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


<display:table name="enrolmentForms" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="member.name" titleKey="enrolment.member" />
	<display:column>
		<a href="actor/showMember.do?actorId=${row.member.id}">
					<spring:message code="actor.profile" />
				</a>
	</display:column>
	<display:column property="enrolment.momentEnrol"
		titleKey="enrolment.momentEnrol" />

	<display:column property="enrolment.momentDropOut"
		titleKey="enrolment.momentDropOut" />

	<display:column>
		<jstl:forEach var="entry" items="${row.enrolment.position.name}">
			<jstl:if test="${lang==entry.key}">
				<jstl:out value="${entry.value}" />
			</jstl:if>
		</jstl:forEach>
	</display:column>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${row.enrolment.accepted==false}">
				<a
					href="enrolment/brotherhood/enrol.do?enrolmentId=${row.enrolment.id}">
					<spring:message code="enrolment.enrol" />
				</a>
			</jstl:if>
		</display:column>
		<display:column>
			<jstl:if test="${row.enrolment.accepted==true}">
				<a
					href="enrolment/brotherhood/dropout.do?enrolmentId=${row.enrolment.id}">
					<spring:message code="enrolment.dropOut" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>