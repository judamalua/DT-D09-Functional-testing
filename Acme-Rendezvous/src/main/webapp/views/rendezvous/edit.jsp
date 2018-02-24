<%--
 * action-1.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<!-- Variables -->
<spring:message code="rendezvous.moment.placeholder"
	var="momentPlaceholder" />
<spring:message code="rendezvous.gpsCoordinates.placeholder"
	var="gpsPlaceholder" />

<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>
<div class="row">
<form:form action="${requestURI}" modelAttribute="rendezvous">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<jstl:if test="${rendezvous.finalMode== null or !rendezvous.finalMode}">
		<acme:textbox code="rendezvous.name" path="name" required="true" />
		<acme:textarea code="rendezvous.description" path="description"
			required="true" />
		<acme:textbox code="rendezvous.moment" path="moment" required="true"
			placeholder="${momentPlaceholder}" />
		<acme:textbox code="rendezvous.pictureUrl" path="pictureUrl" />
		<acme:textbox code="rendezvous.gpsCoordinates" path="gpsCoordinates"
			placeholder="${gpsPlaceholder}" required="true" />
	</jstl:if>
	<acme:select code="rendezvous.similars" path="similars"
		items="${rendezvouses}" itemLabel="name" multiple="true" />

	
	<div class = "cleared-div">
	<jstl:if test="${rendezvous.finalMode== null or !rendezvous.finalMode}">
		<acme:checkbox code="rendezvous.finalMode" path="finalMode" id = "finalMode" />
		<jstl:if test="${adult}">
			<acme:checkbox code="rendezvous.adultOnly" path="adultOnly" id = "adultOnly"/>
		</jstl:if>
	</jstl:if>
	</div>	
	<acme:submit name="save" code="rendezvous.save" />
	<jstl:if test="${rendezvous.id!=0 and !rendezvous.finalMode}">
		<acme:delete clickCode="rendezvous.confirm.delete" name="delete"
			code="rendezvous.delete" />
	</jstl:if>
	<acme:cancel url="rendezvous/user/list.do" code="rendezvous.cancel" />

</form:form>
</div>
