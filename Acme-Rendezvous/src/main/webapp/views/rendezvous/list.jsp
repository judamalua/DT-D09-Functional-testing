<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variable declaration -->
<spring:message code="rendezvous.name" var="titleName" />
<spring:message code="rendezvous.description" var="titleDescription" />
<spring:message code="rendezvous.moment" var="titleMoment" />
<spring:message code="master.page.moment.format" var="formatMoment" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />

<!-- Pagination -->
<acme:pagination requestURI="${requestURI}?anonymous=${anonymous}&page="
	pageNum="${pageNum}" page="${page}" />


<!-- Display -->

<display:table name="rendezvouses" id="rendezvous"
	requestURI="${requestURI}">

	<display:column property="name" title="${titleName}" sortable="true" />
	<display:column property="description" title="${titleDescription}" />
	<display:column property="moment" title="${titleMoment}"
		format="${formatMoment}" sortable="true" />
	<display:column>
		<acme:button
			url="rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvous.id}&anonymous=${anonymous}"
			code="rendezvous.details" />
	</display:column>


	<display:column>
		<jstl:if
			test="${requestURI==\"rendezvous/user/list.do\" and rendezvous.moment>=currentDate}">
			<jstl:if
				test="${!rendezvous.finalMode and !rendezvous.deleted and rendezvous.moment>currentDate}">
				<acme:button
					url="rendezvous/user/edit.do?rendezvousId=${rendezvous.id}"
					code="rendezvous.edit" />
			</jstl:if>
			<jstl:if
				test="${rendezvous.finalMode and !rendezvous.deleted and rendezvous.moment>currentDate}">
				<acme:button
					url="similar/user/edit.do?rendezvousId=${rendezvous.id}"
					code="rendezvous.edit" />
			</jstl:if>
		</jstl:if>
	</display:column>


	<display:column>
		<jstl:if test="${rendezvous.deleted}">
			<i class="material-icons">delete</i>
			<spring:message code="rendezvous.deleted" />
		</jstl:if>
	</display:column>

	<display:column>
		<jstl:if test="${rendezvous.adultOnly}">
			<i class="material-icons">do_not_disturb</i>
			<spring:message code="rendezvous.adultOnly" />
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${!rendezvous.finalMode && !rendezvous.deleted}">
			<i class = "material-icons">mode_edit</i>
			<spring:message code = "rendezvous.list.draft"/>
		</jstl:if>
	</display:column>


	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="rendezvous/admin/delete.do?rendezvousId=${rendezvous.id}"
				code="rendezvous.delete" />
		</security:authorize>
	</display:column>


</display:table>


<!-- Creating a new rendezvous -->

<security:authorize access="hasRole('USER')">
	<acme:button url="rendezvous/user/create.do" code="rendezvous.create" />
</security:authorize>
