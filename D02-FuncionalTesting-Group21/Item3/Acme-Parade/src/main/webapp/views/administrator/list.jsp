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


<display:table name="actors" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="userAccount.username"
		titleKey="actor.username" />

	<display:column property="name" titleKey="actor.name" />

	<display:column property="surname" titleKey="actor.surname" />

	<display:column property="email" titleKey="actor.email" />

	<display:column property="phone" titleKey="actor.phone" />

	<display:column titleKey="actor.spammer" >
		<jstl:if
				test="${row.isSpammer == null }">
				N/A
		</jstl:if>		
		<jstl:if
				test="${row.isSpammer != null}">
				<jstl:out value="${row.isSpammer} "/>
		</jstl:if>		
	
	</display:column>

	<display:column titleKey="actor.polarity" >
		
		<jstl:if
				test="${row.polarityScore == null }">
				N/A
		</jstl:if>		
		<jstl:if
				test="${row.polarityScore != null}">
				<jstl:out value="${row.polarityScore} "/>
		</jstl:if>		
		
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
		    &nbsp;
			<jstl:if
				test="${row.isSpammer == true || row.polarityScore == -1.0 }">
				<jstl:if test="${row.isBanned == false }">
					<a href="actor/administrator/ban.do?actorId=${row.id}"> <spring:message
							code="actor.ban" />
					</a>
				</jstl:if>
				<jstl:if test="${row.isBanned == true}">
					<a href="actor/administrator/unban.do?actorId=${row.id}"> <spring:message
							code="actor.unban" />
					</a>
				</jstl:if>
			</jstl:if>
			&nbsp;
		</display:column>
	</security:authorize>
</display:table>
<br>
<a href="actor/administrator/findSpammers.do"> <spring:message
		code="master.page.administrator.findSpammers" />
</a>
<br>
<a href="actor/administrator/calculatePolarity.do"> <spring:message
		code="master.page.administrator.calculatePolarity" />
</a>
<br>
