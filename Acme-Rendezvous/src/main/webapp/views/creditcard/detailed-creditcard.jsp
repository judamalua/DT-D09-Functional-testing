<%--
 * detailed-creditcard.jsp
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
<h5>
	<spring:message code="creditcard.holdername" />
</h5>
<p>
	<jstl:out value="${creditCard.holderName}" />
</p>
<br />
<h5>
	<spring:message code="creditcard.brandname" />
</h5>
<p>
	<jstl:out value="${creditCard.brandName}" />
</p>
<br />
<h5>
	<spring:message code="creditcard.number" />
</h5>
<p>
	<jstl:out value="${creditCard.number}" />
</p>
<br />
<h5>
	<spring:message code="creditcard.expirationMonth" />
</h5>
<p>
	<jstl:out value="${creditCard.expirationMonth}" />
</p>
<br />
<h5>
	<spring:message code="creditcard.expirationYear" />
</h5>
<p>
	<jstl:out value="${creditCard.expirationYear}" />
</p>
<br />
<h5>
	<spring:message code="creditcard.cvv" />
</h5>
<p>
	<jstl:out value="${creditCard.cvv}" />
</p>
<br />

