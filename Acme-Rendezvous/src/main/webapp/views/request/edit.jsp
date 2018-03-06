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

<script type="text/javascript">
function useOldCreditCard(){
	var creditCardId = document.getElementsByClassName("oldCreditCardId")[0].innerHTML;
	var language = getCookie("language");
	document.getElementsByClassName("cardForm")[0].innerHTML='<input id="creditCard" name="creditCard" type="hidden" value="'+creditCardId+'">';
	document.getElementById("creditCardButton")[0].onclick = function () { useNewCreditCard(); };
	
	if (language == "es") {
		document.getElementById("creditCardButton")[0].innerHTML="Usar una nueva tarjeta";
	} else {
		document.getElementById("creditCardButton")[0].innerHTML="Use a new credit card";
	}
}
function useNewCreditCard(){
	document.getElementsByClassName("cardForm")[0].innerHTML='<input id="creditCard.id" name="creditCard.id" type="hidden" value="0"><input id="creditCard.version" name="creditCard.version" type="hidden" value="0"><input id="creditCard.cookieToken" name="creditCard.cookieToken" type="hidden" value=""><div><div class="row"><div class="input-field col s3"><label for="creditCard.holderName" class="">Holder name*</label><input id="creditCard.holderName" name="creditCard.holderName" type="text" value="">	</div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.brandName">Brand name*</label><input id="creditCard.brandName" name="creditCard.brandName" type="text" value="">	</div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.number">Number*</label><input id="creditCard.number" name="creditCard.number" type="text" value=""></div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.expirationMonth" class="active">Expiration month*</label><input id="creditCard.expirationMonth" name="creditCard.expirationMonth" placeholder="MM" type="text" value="">	</div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.expirationYear" class="active">Expiration year*</label><input id="creditCard.expirationYear" name="creditCard.expirationYear" placeholder="yy" type="text" value=""></div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.cvv">CVV*</label><input id="creditCard.cvv" name="creditCard.cvv" type="text" value=""></div></div></div>';
	document.getElementById("creditCardButton")[0].onclick = function () { useOldCreditCard(); };
	
	if (language == "es") {
		document.getElementById("creditCardButton")[0].innerHTML="Usar esta tarjeta";
	} else {
		document.getElementById("creditCardButton")[0].innerHTML="Use this card";
	}
}
function checkCreditCard(){
	var cardCookie = getCookie("cardCookie");
	var language = getCookie("language");
	if(cardCookie!=null){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				if (language == "es") {
					document.getElementsByClassName("cookieCard")[0].innerHTML = '<p>Usar esta tarjeta de crédito anterior:</p><p>XXXX XXXX XXXX '+this.responseText.substring(0,4)+'</p><button onClick="useOldCreditCard()" class="creditCardButton">Usar esta tarjeta<button><p hidden="true" class="oldCreditCardId">'+this.responseText.substring(4)+'</p>';
				} else {
					document.getElementsByClassName("cookieCard")[0].innerHTML = '<p>Use this previous credit card:</p><p>XXXX XXXX XXXX '+this.responseText.substring(0,4)+'</p><button onClick="useOldCreditCard()" class="creditCardButton">Use this card<button><p hidden="true" class="oldCreditCardId">'+this.responseText.substring(4)+'</p>';
				}
			}
		};
		xhttp.open("GET", "/Acme-Rendezvous/request/user/ajaxCard.do", true);
		xhttp.send();
	}
}
window.onload = function() {
	  checkCreditCard();
	};
</script> 

<spring:message code="request.creditcard.expirationYear.placeholder" var="expirationYearPlaceholder" />	
<spring:message code="request.creditcard.info" var="creditCardInfo" />
<spring:message code="request.creditcard.info" var="creditCardInfo" />

	
<p><em><spring:message code = "form.required.params"/></em></p>

<form:form id="form" action="request/user/edit.do" modelAttribute="request">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	<form:hidden path="service"/>
	
	<acme:textarea code="request.comment" path="comment"/>
	
	<spring:message code="request.rendezvous.select" />
	
	<div class="input-field col s3">
		<select id="rendezvous" name="rendezvous">
  			<jstl:forEach var="i" items="${rendezvouses}">
  				<option value="${i.id}"><jstl:out value="${i.name}"/></option>
  			</jstl:forEach>
		</select> 
	</div>
	
	
	
	<h4><jstl:out value="${creditCardInfo}"/></h4>
	
	<div class="cookieCard"></div>
	
	<div class="cardForm">
	
	<form:hidden path="creditCard.id"/>
	<form:hidden path="creditCard.version"/>
	<form:hidden path="creditCard.cookieToken"/>
	
	<acme:textbox code="request.creditcard.holderName" path="creditCard.holderName" required="true" />
	
	<acme:textbox code="request.creditcard.brandName" path="creditCard.brandName" required="true" />
	
	<acme:textbox code="request.creditcard.number" path="creditCard.number" required="true" />
	
	<acme:textbox code="request.creditcard.expirationMonth" path="creditCard.expirationMonth" 
		required="true" placeholder ="MM"/>
	
	<acme:textbox code="request.creditcard.expirationYear" path="creditCard.expirationYear" 
		required="true" placeholder ="${expirationYearPlaceholder}"/>
	
	<acme:textbox code="request.creditcard.cvv" path="creditCard.cvv" required="true" />
	
	</div>
	
	
	<acme:submit name="save" code="request.save"/>
	
	<acme:cancel url="service/list.do?anonymous=false" code="request.cancel"/>
	
</form:form>