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

<spring:message code="path.createdPath" />
<br>
<display:table name="paths" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="parade.ticker" titleKey="path.parade.ticker" />
	<display:column property="parade.title" titleKey="path.parade.title" />

</display:table>
<br>
<br>
<spring:message code="path.createPath" />
<br>
<display:table name="parades" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="ticker" titleKey="path.parade.ticker" />
	<display:column property="title" titleKey="path.parade.title" />
	<display:column>
		<a href="path/brotherhood/create.do?paradeId=${row.id}"> <spring:message
				code="path.create" />
		</a>
	</display:column>
</display:table>