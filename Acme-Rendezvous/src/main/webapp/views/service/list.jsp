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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variable declaration -->
<spring:message code="service.pictureUrl" var="titlePicture" />
<spring:message code="service.name" var="titleName" />
<spring:message code="service.description" var="titleDescription" />
<spring:message code="service.price" var="titlePrice" />
<spring:message code="service.request" var="requestColumn" />
<spring:message code="service.request.create" var="createRequest" />
<spring:message code="service.request.list.title" var="titleListRequests" />

<!-- Pagination -->
<acme:pagination requestURI="${requestURI}page=" pageNum="${pageNum}"
	page="${page}" />


<!-- Display -->

<display:table name="services" id="service" requestURI="${requestURI}page=${page}">

	<display:column title="${titlePicture}">
		<jstl:if test="${service.pictureUrl!=null}">
			<img class="serviceImg" src="${service.pictureUrl}" />
		</jstl:if>
	</display:column>
	<display:column property="name" title="${titleName}" sortable="true" />
	<display:column property="description" title="${titleDescription}" />
	<display:column title="${titleCategories}" />
		<display:column>
			<acme:button url="category/list.do?serviceId=${service.id}"
				code="service.categories.see" />
	</display:column>
	
	<display:column title="${titleListRequests}">
		<security:authorize access="hasRole('MANAGER')">
			<jstl:if test="${fn:length(service.requests) != 0}">
				<acme:button url="request/manager/list.do?serviceId=${service.id}"
				code="service.request.list" />
			</jstl:if>	
		</security:authorize>
	</display:column>
	
	<display:column>
		<jstl:if
			test="${!service.cancelled && managedServices[service_rowNum-1] }">
			<acme:button url="service/manager/edit.do?serviceId=${service.id}"
				code="service.edit" />
		</jstl:if>
	</display:column>
	
	<display:column>
		<security:authorize access="hasRole('USER')">
			<acme:button url="request/user/edit.do?serviceId=${service.id}"
				code="service.edit" />
		</security:authorize>
	</display:column>

	<display:column>
		<jstl:if test="${service.cancelled}">
			<i class="material-icons">delete</i>
			<spring:message code="service.cancelled" />
		</jstl:if>
	</display:column>

	<security:authorize access="hasRole('USER')">
		<display:column title="${requestColumn}">
			<acme:button url="request/user/create.do?serviceId=${service.id}"
				code="service.request.create" />
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${!service.cancelled}">
			<display:column>
				<acme:button url="service/admin/cancel.do?serviceId=${service.id}"
					code="service.cancel" />
			</display:column>
		</jstl:if>
	</security:authorize>

</display:table>

<!-- Creating a new service -->

<security:authorize access="hasRole('MANAGER')">
	<acme:button url="service/manager/create.do" code="service.create" />
</security:authorize>
