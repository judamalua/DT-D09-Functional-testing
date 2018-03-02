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
	<form:form action="category/admin/edit.do" modelAttribute="category">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="category.name" path="name" required="true" />
		<acme:textarea code="category.description" path="description"
			required="true" />
		<acme:select items="${categories}" itemLabel="name" code="category.fatherCategory" path="fatherCategory"/>

		<acme:submit name="save" code="category.save" />
		<jstl:if test="${category.id!=0 and !category.cancelled}">
			<acme:delete clickCode="category.confirm.delete" name="delete"
				code="category.delete" />
		</jstl:if>
		<acme:cancel url="category/admin/list.do" code="category.cancel" />

	</form:form>
</div>
