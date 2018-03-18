<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
<%@ attribute name="creditCard" required="true" type="domain.CreditCard" %>

<div class="jp-card-container">
	<div class="jp-card jp-card-visa jp-card-identified creditCard${creditCard.id}">
		<div class="jp-card-front">
			<div class="jp-card-logo jp-card-elo">
				<div class="e">e</div>
				<div class="l">l</div>
				<div class="o">o</div></div>
			<div class="jp-card-logo jp-card-visa">${creditCard.brandName}</div>
			<div class="jp-card-logo jp-card-visaelectron">Visa<div class="elec">Electron</div></div>
			<div class="jp-card-logo jp-card-mastercard">Mastercard</div>
			<div class="jp-card-logo jp-card-maestro">Maestro</div>
			<div class="jp-card-logo jp-card-amex"></div>
			<div class="jp-card-logo jp-card-discover">discover</div>
			<div class="jp-card-logo jp-card-dinersclub"></div>
			<div class="jp-card-logo jp-card-dp"></div>
			<div class="jp-card-logo jp-card-dankort">
				<div class="dk"><div class="d"></div>
				<div class="k"></div></div></div>
			<div class="jp-card-logo jp-card-jcb">
				<div class="j">J</div>
				<div class="c">C</div>
				<div class="b">B</div></div>
			<div class="jp-card-lower">
			<div class="jp-card-shiny"></div>
			<div class="jp-card-cvc jp-card-display jp-card-valid">${creditCard.cvv}</div>
			<div class="jp-card-number jp-card-display jp-card-valid">${creditCard.number}</div>
			<div class="jp-card-name jp-card-display jp-card-valid">${creditCard.holderName}</div>
			<div class="jp-card-expiry jp-card-display jp-card-valid" data-before="month/year" data-after="valid thru">${creditCard.expirationMonth}/${creditCard.expirationYear}</div>
			</div></div><div class="jp-card-back">
			<div class="jp-card-bar"></div>
			<div class="jp-card-cvc jp-card-display jp-card-valid">${creditCard.cvv}</div>
			<div class="jp-card-shiny"></div>
			</div></div></div>
<button class="btn" onClick="flipCard(${creditCard.id})" align="center"><spring:message code="creditCard.flip" /></button>