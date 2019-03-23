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


<display:table name="segments" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="originLatitude"
		titleKey="path.originLatitude" />
	<display:column property="originLongitude"
		titleKey="path.originLongitude" />
	<display:column property="approximateTimeOri"
		titleKey="path.approximateTimeOri" />
	<display:column property="destinationLatitude"
		titleKey="path.destinationLatitude" />
	<display:column property="destinationLongitude"
		titleKey="path.destinationLongitude" />
	<display:column property="approximateTimeDes"
		titleKey="path.approximateTimeDes" />
</display:table>

<br>
<br>
<jstl:if test="${draft == true}">
	<a href="segment/brotherhood/create.do?pathId=${pathId}"> <spring:message
			code="segment.add" />
	</a>
	<br>
	<br>
	<jstl:if test="${isEmpty == false}">
		<a href="segment/brotherhood/deleteLast.do?pathId=${pathId}"> <spring:message
				code="segment.deleteLast" />
		</a>
	</jstl:if>
</jstl:if>