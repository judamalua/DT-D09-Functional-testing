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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variable declaration -->
<spring:message code="category.name" var="titleName" />
<spring:message code="category.description" var="titleDescription" />
<spring:message code="category.fatherCategory" var="titleFather" />

<jstl:if test="${categoryId!=null}">
	<acme:button url="category/list.do?categoryId=${categoryId}" code="category.upperCategory"/>
</jstl:if>
<jstl:if test="${categoryId==null}">
	<acme:button url="category/list.do" code="category.upperCategory"/>
</jstl:if>

<!-- Pagination -->
<acme:pagination requestURI="${requestURI}page=" pageNum="${pageNum}"
	page="${page}" />

<!-- Display -->

<display:table name="categories" id="category"
	requestURI="${requestURI}">

	<display:column property="name" title="${titleName}" sortable="true" />
	<display:column property="description" title="${titleDescription}" />
	<display:column property="fatherCategory.name" title="${titleFather}" />
	<display:column>
		<acme:button url="category/list.do?categoryId=${category.id}"
			code="category.subCategories" />
	</display:column>

	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<jstl:if test="${fn:length(category.services)==0}">
				<acme:button url="category/admin/edit.do?categoryId=${category.id}"
					code="category.edit" />
			</jstl:if>
		</security:authorize>
	</display:column>
</display:table>

<!-- Creating a new category -->

<security:authorize access="hasRole('ADMIN')">
	<acme:button url="category/admin/create.do" code="category.create" />
</security:authorize>
