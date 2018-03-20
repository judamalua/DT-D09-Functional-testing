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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<spring:message code="request.creditcard.expirationYear.placeholder" var="expirationYearPlaceholder" />	
<spring:message code="request.creditcard.info" var="creditCardInfo" />
<spring:message code="request.creditcard.info" var="creditCardInfo" />

	
<p><em><spring:message code = "form.required.params"/></em></p>

<div class = "row">

<form:form id="form" action="request/user/edit.do" modelAttribute="request">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	<form:hidden path="service"/>
	<acme:textarea code="request.comment" path="comment" required = "true"/>
	
	
	<jstl:if test="${fn:length(rendezvouses) != 0}">
		<div class="input-field col s3">
			<select id="rendezvous" name="rendezvous">
  				<jstl:forEach var="i" items="${rendezvouses}">
  					<option value="${i.id}"><jstl:out value="${i.name}"/></option>
  				</jstl:forEach>
			</select>
			<label for = "rendezvous"><spring:message code="request.rendezvous.select" />*</label> 
		</div>
	</jstl:if>
	
	<jstl:if test="${fn:length(rendezvouses) == 0}">
		<div class="error">
			<spring:message code="request.rendezvouses.empty"/>
		</div>
	</jstl:if>
	
	
	<div class = "cleared-div">
	</div>
	
	<div class = "cleared-div">
	<h4><jstl:out value="${creditCardInfo}"/></h4>
	</div>
	
	<div class="cookieCard"></div>
	<p class="creditCardCookieTokenNew" hidden="true"></p>
	<div class="cardForm">
	
	<form:hidden path="creditCard.id"/>
	<form:hidden path="creditCard.version"/>
	<form:hidden path="creditCard.cookieToken" class="creditCardCookieToken"/>
	
	<acme:textbox code="request.creditcard.holderName" path="creditCard.holderName" required="true" />
	
	<acme:textbox code="request.creditcard.brandName" path="creditCard.brandName" required="true" />
	
	<acme:textbox code="request.creditcard.number" path="creditCard.number" required="true" />
	
	<acme:textbox code="request.creditcard.expirationMonth" path="creditCard.expirationMonth" 
		required="true" placeholder ="MM"/>
	
	<acme:textbox code="request.creditcard.expirationYear" path="creditCard.expirationYear" 
		required="true" placeholder ="${expirationYearPlaceholder}"/>
	
	<acme:textbox code="request.creditcard.cvv" path="creditCard.cvv" required="true" />
	
	</div>
	
	<jstl:if test="${fn:length(rendezvouses) != 0}">
		<button type="submit" name="save" class="btn" id = "submit" onclick="saveCreditCardCookie()">
			<spring:message code="request.save" />
		</button>
	</jstl:if>

	<jstl:if test="${fn:length(rendezvouses) == 0}">
		<button type="submit" name="save" class="btn" id = "submit" onclick="saveCreditCardCookie()" disabled>
			<spring:message code="request.save" />
		</button>
	</jstl:if>
	
	<acme:cancel url="service/list.do?anonymous=false" code="request.cancel"/>
	
</form:form>

</div>
<script src="scripts/creditCardAjax.js"></script>
<script type="text/javascript">
window.onload = function() {
	checkCreditCard();
	getBusinessName();
	checkCookie();
};
</script> 