<%--
 * list.jsp
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
<spring:message code="request.service" var="titleService" />
<spring:message code="request.creditCard" var="titleCreditCard" />
<spring:message code="master.page.moment.format" var="formatMoment" />
<spring:message code="request.comment" var="titleComment" />


<display:table name="requests" id="request" requestURI="${requestURI}">
	
	<display:column title="${titleService}" >
		<jstl:out value="${request.service.name}"/>
	</display:column>
	
	<security:authorize access="hasRole('USER')">
		<display:column title="${titleCreditCard}">
			<a href="creditCard/user/display.do?creditCardId=${request.creditCard.id}"><jstl:out value="${request.creditCard.number}"/></a>
		</display:column>
	</security:authorize>
	
	<display:column property="moment" title="${titleMoment}" format="${formatMoment}" sortable="true"/>
	
	<display:column property="comment" title="${titleComment}" />
</display:table>