<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form id = "form" action="actor/admin/register.do" modelAttribute ="admin">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<p><em><spring:message code = "form.required.params"/></em></p>
	
	<acme:textbox code="admin.name" path="name" required = "true"/>
	
	<acme:textbox code="admin.surname" path="surname" required = "true"/>
	
	<acme:textbox code="admin.phoneNumber" path="phoneNumber"/>
	
	<acme:textbox code="admin.postalAddress" path="postalAddress"/>
	
	<acme:textbox code="admin.email" path="email" required = "true"/>
	
	<acme:textbox code="admin.birthDate" path="birthDate" required = "true" placeholder = "dd/MM/yyyy"/>
	
	<acme:textbox code="admin.username" path="userAccount.username" required = "true"/>
	
	<acme:password code="admin.password" path="userAccount.password" required = "true"/>
	
	<acme:confirmPassword name="confirmPassword" code="admin.confirm.password" required = "true"/>
	
	<div>
	<acme:submit name="save" code="admin.save"/>
		
	<acme:cancel url="/" code="admin.cancel"/>
	</div>

</form:form>
