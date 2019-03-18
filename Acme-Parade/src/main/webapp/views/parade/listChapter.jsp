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


<display:table name="parades" id="row" requestURI="${requestURI}" pagesize="5"
	class="displaytag">

	<display:column property="ticker" titleKey="parade.ticker" />

	<display:column property="title" titleKey="parade.title" />

	<display:column property="moment" titleKey="parade.moment" />

	<jstl:if test="${row.status == 'ACCEPTED' }">
		<display:column property="status" class="${'GREEN'}"
			titleKey="parade.status" />
		<display:column>
			&nbsp;
	</display:column>
	</jstl:if>
	<jstl:if test="${row.status == 'REJECTED' }">
		<display:column property="status" class="${'RED'}"
			titleKey="parade.status" />
		<display:column>
			&nbsp;
	</display:column>
	</jstl:if>
	<jstl:if test="${row.status == 'SUBMITTED' }">
		<display:column property="status" class="${'GREY'}"
			titleKey="parade.status" />
		<display:column>
			<a href="parade/chapter/reject.do?paradeId=${row.id}"> <spring:message
					code="parade.show" />
			</a>
			<br>
			<a href="parade/chapter/accepted.do?paradeId=${row.id}"> <spring:message
					code="parade.show" />
			</a>
		</display:column>
	</jstl:if>


</display:table>
