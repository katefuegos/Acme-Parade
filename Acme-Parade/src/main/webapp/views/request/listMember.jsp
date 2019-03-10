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

<spring:message code="request.approved" />
<br>
<display:table name="requestsApproved" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="procession.title" class="${'GREEN'}"
		titleKey="request.procession" />

	<display:column property="roow" titleKey="request.row" />

	<display:column property="coluumn" titleKey="request.column" />

</display:table>
<br>
<br>
<spring:message code="request.rejected" />
<br>
<display:table name="requestsRejected" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="procession.title" class="${'ORANGE'}"
		titleKey="request.procession" />

	<display:column property="reasonReject" titleKey="request.reasonReject" />

</display:table>
<br>
<br>
<spring:message code="request.pending" />
<br>
<display:table name="requestsPending" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	<display:column property="procession.title" class="${'GREY'}"
		titleKey="request.procession" />

	<display:column>
		<a href="request/member/delete.do?requestId=${row.id}"> <spring:message
				code="request.delete" />
		</a>
	</display:column>
</display:table>
<br>
<br>
<spring:message code="request.processionsNot" />:
<br>
<display:table name="processions" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="title"
		titleKey="request.procession" />

	<display:column>
		<a href="request/member/request.do?processionId=${row.id}"> <spring:message
				code="request.request" />
		</a>
	</display:column>
</display:table>