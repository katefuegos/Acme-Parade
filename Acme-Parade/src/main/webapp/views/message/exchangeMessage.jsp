<%--
 * action-1.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="message/actor/edit.do" modelAttribute="messageForm">

	<form:hidden path="message.id" />
	<form:hidden path="message.version" />
	<form:hidden path="message.moment" />
	<form:hidden path="message.sender" />
	<jstl:if test="${isRead==true}">
		<spring:message code="message.sender" />:
		<jstl:out value="${messageForm.message.sender.userAccount.username}"></jstl:out>
		<br />

		<form:hidden path="message.recipient" />
		<spring:message code="message.receiver" />:
		<jstl:out
			value="${messageForm.message.recipient.userAccount.username}"></jstl:out>
		<br />
		<jstl:if test="${isMove==false }">
			<form:hidden path="message.box" />
			<spring:message code="message.box" />:
			<jstl:out value="${messageForm.message.box.name}"></jstl:out>
			<br />
		</jstl:if>
		<jstl:if test="${isMove==true }">
			<form:label path="message.box">
				<spring:message code="message.box" />:
			</form:label>
			<form:select id="boxes" path="message.box">
				<form:options items="${boxes}" itemValue="id" itemLabel="name" />
			</form:select>
			<form:errors cssClass="error" path="message.box" />
			<br />
		</jstl:if>

		<form:label path="message.priority">
			<spring:message code="message.priority" />:
		</form:label>
		<form:input path="message.priority" readonly="${isRead}" />
		<form:errors cssClass="error" path="message.priority" />
		<br />

		<form:label path="message.moment">
			<spring:message code="message.moment" />:
		</form:label>
		<form:input path="message.moment" readonly="${isRead}" />
		<form:errors cssClass="error" path="message.moment" />
		<br />



	</jstl:if>
	<form:label path="message.subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:textarea path="message.subject" readonly="${isRead}" />
	<form:errors cssClass="error" path="message.subject" />
	<br />
	<form:label path="message.body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="message.body" readonly="${isRead}" />
	<form:errors cssClass="error" path="message.body" />
	<br />
	<form:label path="message.tags">
		<spring:message code="message.tags" />:
	</form:label>
	<form:textarea path="message.tags" readonly="${isRead}" />
	<form:errors cssClass="error" path="message.tags" />
	<br />


	<jstl:if test="${isRead!=true}">
		<form:hidden path="message.box" />
		<form:label path="message.priority">
			<spring:message code="message.priority" />:
	</form:label>
		<form:select id="priorities" path="message.priority">
			<form:options items="${priorities}" />
		</form:select>
		<form:errors cssClass="error" path="message.priority" />


		<form:label path="message.recipient">
			<spring:message code="message.receiver" />:
	</form:label>
		<form:select id="receivers" path="message.recipient">
			<form:options items="${receivers}" itemValue="id"
				itemLabel="userAccount.username" />
		</form:select>
		<form:errors cssClass="error" path="message.recipient" />
		<br />
	</jstl:if>
	<jstl:if test="${isRead!=true || (isMove==true && isRead==true)}">
		<input type="submit" name="save"
			value="<spring:message code="message.save" />" />&nbsp; 
		<jstl:if test="${messageForm.message.id!=0}">
			<input type="submit" name="delete"
				value="<spring:message code="message.delete" />"
				onclick="javascript: return confirm('<spring:message code="message.confirm.delete" />')" />
		</jstl:if>
		<input type="button" name="cancel"
			value="<spring:message code="message.cancel" />"
			onclick="javascript: relativeRedir('box/actor/list.do');" />
		<br />

	</jstl:if>



</form:form>
<p>
	<jstl:out value="${oops }" />
</p>