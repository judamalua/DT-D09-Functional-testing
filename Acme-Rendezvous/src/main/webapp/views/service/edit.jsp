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

<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>
<div class="row">
	<form:form action="service/manager/edit.do" modelAttribute="service">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="service.name" path="name" required="true" />
		<acme:textarea code="service.description" path="description"
			required="true" />
		<acme:textbox code="service.pictureUrl" path="pictureUrl"/>
		<acme:textbox code="service.price" path="price" required="true" />
		<acme:select items="${categories}" itemLabel="name" code="service.categories" path="categories" multiple="true"/>

		<acme:submit name="save" code="service.save" />
		<jstl:if test="${service.id!=0 and !service.cancelled}">
			<acme:delete clickCode="service.confirm.delete" name="delete"
				code="service.delete" />
		</jstl:if>
		<acme:cancel url="service/manager/list.do" code="service.cancel" />

	</form:form>
</div>
