<%--
 * edit.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


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


<display:table name="boxes" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column property="name" titleKey="box.name" />

	<display:column titleKey="box.rootbox">
		<jstl:if test="${row.rootbox!=null}">
			<jstl:out value="${row.rootbox.name}" />
		</jstl:if>
	</display:column>
	<display:column titleKey="box.subBoxes">
		<jstl:forEach var="entry" items="${row.subboxes}">
			<jstl:out value="${entry.name}" />&nbsp;&nbsp;
		</jstl:forEach>
	</display:column>


	<display:column titleKey="box.message">
		<a href="message/actor/list.do?boxId=${row.id}"> <spring:message
				code="box.show" />
		</a>
	</display:column>


	
		<display:column>
		<jstl:if test="${row.isSystem != true }">
			<a href="box/actor/edit.do?boxId=${row.id}"> <spring:message
					code="box.edit" />
			</a>
				</jstl:if>
		</display:column>


</display:table>

<a href="box/actor/create.do"> <spring:message code="box.create" /></a>



