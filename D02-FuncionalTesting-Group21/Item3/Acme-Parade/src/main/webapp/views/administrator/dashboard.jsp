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
	<spring:message code="administrator.dashboard.C1" />
</h3>

<ul>
	<li><spring:message code="administrator.avg" />: <jstl:out
			value="${avgC1}" /></li>
	<li><spring:message code="administrator.max" />: <jstl:out
			value="${maxC1}" /></li>
	<li><spring:message code="administrator.min" />: <jstl:out
			value="${minC1}" /></li>
	<li><spring:message code="administrator.stddev" />: <jstl:out
			value="${stddevC1}" /></li>
</ul>
<br />
<br />
<h3>
	<spring:message code="administrator.dashboard.C2" />
</h3>

<p>
	<spring:message code="administrator.dashboard.name" />
	:
	<jstl:out value="${nameLargest}" />
</p>
<p>
	<spring:message code="administrator.dashboard.count" />
	:
	<jstl:out value="${countLargest}" />
</p>
<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.C3" />
</h3>

<p>
	<spring:message code="administrator.dashboard.name" />
	:
	<jstl:out value="${nameSmallest}" />
</p>
<p>
	<spring:message code="administrator.dashboard.count" />
	:
	<jstl:out value="${countLargest}" />
</p>
<br />
<br />


<h3>
	<spring:message code="administrator.dashboard.C4" />
</h3>
<jstl:if test="${statusCount!='n/a'}">
<jstl:forEach var="entry" items="${statusCount}">
	<jstl:out value="${entry.key}" /> --- <jstl:out value="${entry.value}" />
	<br>
</jstl:forEach></jstl:if><jstl:if test="${statusCount=='n/a'}">&nbsp;&nbsp;&nbsp;<jstl:out value="${statusCount}" /></jstl:if>
<br />
<br />


<h3>
	<spring:message code="administrator.dashboard.C5" />
</h3>
<display:table name="paradesC5" id="row" class="displaytag">
	<display:column property="ticker"
		titleKey="administrator.parade.ticker" />
	<display:column property="title"
		titleKey="administrator.parade.title" />
	<display:column property="moment"
		titleKey="administrator.parade.moment" />
	<display:column property="brotherhood.title"
		titleKey="administrator.parade.parade.brotherhood" />
</display:table>

<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.C7" />
</h3>
<display:table name="queryC7" id="row" class="displaytag">
	<display:column property="name" titleKey="actor.name" />
	<display:column property="userAccount.username"
		titleKey="actor.username" />
	<display:column property="email" titleKey="actor.email" />
	<display:column property="phone" titleKey="actor.phone" />
</display:table>
<br />
<br />
 <h3>
	<spring:message code="administrator.dashboard.C8" />
</h3>
<display:table name="position" id="row" class="displaytag">
	<display:column >

	<jstl:forEach var="entry" items="${row.position.name}">
		<jstl:if test="${lang==entry.key}">
			<jstl:out value="${entry.value}" />
		</jstl:if>
	</jstl:forEach>
 
	</display:column>
	<display:column property="count"
		/>
	
</display:table>





<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.B1" />
</h3>
<ul>
	<li><spring:message code="administrator.avg" />: <jstl:out
			value="${avgB1}" /></li>
	<li><spring:message code="administrator.max" />: <jstl:out
			value="${maxB1}" /></li>
	<li><spring:message code="administrator.min" />: <jstl:out
			value="${minB1}" /></li>
	<li><spring:message code="administrator.stddev" />: <jstl:out
			value="${stddevB1}" /></li>
</ul>

<display:table name="areaQueryB1" id="row" class="displaytag">
	<display:column property="name" titleKey="administrator.dashboard.name" />
	<display:column property="ratio" titleKey="administrator.ratio" />
	<display:column property="count"
		titleKey="administrator.dashboard.count" />
</display:table>


<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.B2" />
</h3>
<ul>
	<li><spring:message code="administrator.avg" />: <jstl:out
			value="${avgB2}" /></li>
	<li><spring:message code="administrator.max" />: <jstl:out
			value="${maxB2}" /></li>
	<li><spring:message code="administrator.min" />: <jstl:out
			value="${minB2}" /></li>
	<li><spring:message code="administrator.stddev" />: <jstl:out
			value="${stddevB2}" /></li>
</ul>
<br />
<br />
<h3>
	<spring:message code="administrator.dashboard.B3" />
</h3>
<ul>
	<li><spring:message code="administrator.ratio.finder.empty" />:<jstl:out
			value="${queryB3FinderResultEmpty}" /></li>
	<li><spring:message code="administrator.ratio.finder.notEmpty" />:
		<jstl:out value="${queryB3FinderResultNotEmpty}" /></li>
</ul>

<!-- ---
----------------------------------------------------
----------------------------------------------------
 -->

<h3>
	<spring:message code="administrator.dashboard.newC1" />
</h3>
<ul>
	<li><spring:message code="administrator.avg" />: <jstl:out
			value="${avgNewC1}" /></li>
	<li><spring:message code="administrator.max" />: <jstl:out
			value="${maxNewC1}" /></li>
	<li><spring:message code="administrator.min" />: <jstl:out
			value="${minNewC1}" /></li>
	<li><spring:message code="administrator.stddev" />: <jstl:out
			value="${stddevNewC1}" /></li>
</ul>
<br />
<br /> 
 
 
<h3>
	<spring:message code="administrator.dashboard.newC2" />
</h3>
<display:table name="queryNewC2" id="row" class="displaytag">
<display:column property="title" titleKey="administrator.dashboard.name" />
	<display:column property="name" titleKey="actor.name" />
	<display:column property="userAccount.username"
		titleKey="actor.username" />
	<display:column property="email" titleKey="actor.email" />
	<display:column property="phone" titleKey="actor.phone" />
</display:table>
<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.newC3" />
</h3>
<display:table name="queryNewC3" id="row" class="displaytag">
<display:column property="title" titleKey="administrator.dashboard.name" />
	<display:column property="name" titleKey="actor.name" />
	<display:column property="userAccount.username"
		titleKey="actor.username" />
	<display:column property="email" titleKey="actor.email" />
	<display:column property="phone" titleKey="actor.phone" />
</display:table>
<br />
<br />
 
<h3>
	<spring:message code="administrator.dashboard.newB1" />
</h3>
<p>
	<spring:message code="administrator.ratio" />
	:
	<jstl:out value="${queryNewB1}" />
</p>
<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.newB2" />
</h3>
<ul>
	<li><spring:message code="administrator.avg" />: <jstl:out
			value="${avgNewB2}" /></li>
	<li><spring:message code="administrator.max" />: <jstl:out
			value="${maxNewB2}" /></li>
	<li><spring:message code="administrator.min" />: <jstl:out
			value="${minNewB2}" /></li>
	<li><spring:message code="administrator.stddev" />: <jstl:out
			value="${stddevNewB2}" /></li>
</ul>
<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.newB3" />
</h3>
<display:table name="queryNewB3" id="row" class="displaytag">
<display:column property="title" titleKey="administrator.dashboard.name" />
	<display:column property="name" titleKey="actor.name" />
	<display:column property="userAccount.username"
		titleKey="actor.username" />
	<display:column property="email" titleKey="actor.email" />
	<display:column property="phone" titleKey="actor.phone" />
</display:table>
<br />
<br />



<h3>
	<spring:message code="administrator.dashboard.newB4" />
</h3>
<ul><!-- administrator.newB4.ratioDraftMode -->
	<li><spring:message code="administrator.newB4.ratioDraftMode" />:<jstl:out
			value="${ratioDraftMode}" /></li>
	<li><spring:message code="administrator.newB4.ratioFinalMode" />:
		<jstl:out value="${ratioFinalMode}" /></li>
</ul>
<br />
<br />

<h3>
	<spring:message code="administrator.dashboard.newB5" />
</h3>
<display:table name="queryNewB5" id="row" class="displaytag">
	<display:column property="name" titleKey="actor.name" />
	<display:column property="ratio" titleKey="administrator.ratio" />
</display:table>
<br />
<br />

