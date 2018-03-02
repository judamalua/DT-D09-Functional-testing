<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<spring:message code="request.creditcard.expirationYear.placeholder"
	var="expirationYearPlaceholder" />
	
<spring:message code="request.creditcard.info"
	var="creditCardInfo" />
	
<p><em><spring:message code = "form.required.params"/></em></p>

<form:form id="form" action="${requestURI}" modelAttribute="request">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	
	<form:hidden path="creditCard.id"/>
	<form:hidden path="creditCard.version"/>
	
	<acme:textarea code="request.comment" path="comment"/>
	
	
	<h3><jstl:out value="${creditCardInfo}"/></h3>
	
	
	<acme:textbox code="request.creditcard.holderName" path="creditCard.holderName" required="true" />
	
	<acme:textbox code="request.creditcard.brandName" path="creditCard.brandName" required="true" />
	
	<acme:textbox code="request.creditcard.number" path="creditCard.number" required="true" />
	
	<acme:textbox code="request.creditcard.expirationMonth" path="creditCard.expirationMonth" 
		required="true" placeholder ="MM"/>
	
	<acme:textbox code="request.creditcard.expirationYear" path="creditCard.expirationYear" 
		required="true" placeholder ="${expirationYearPlaceholder}"/>
	
	<acme:textbox code="request.creditcard.cvv" path="creditCard.cvv" required="true" />
	
	
	<acme:submit name="save" code="request.save"/>
	
	<acme:cancel url="service/actor/list.do" code="request.cancel"/>
	
</form:form>