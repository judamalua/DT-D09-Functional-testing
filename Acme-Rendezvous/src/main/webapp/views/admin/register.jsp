<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form id = "form" action="actor/admin/register.do" modelAttribute ="admin">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="userAccount.authorities"/>
	
	
	<form:label path="name">
		<spring:message code="admin.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	
	
	<form:label path="surname">
		<spring:message code="admin.surname"/>
	</form:label>
	<form:input path="surname"/>
	<form:errors cssClass="error" path="surname"/>
	
	
	<form:label path="phoneNumber">
		<spring:message code="admin.phoneNumber"/>
	</form:label>
	<form:input path="phoneNumber"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	
	<form:label path="postalAddress">
		<spring:message code="admin.postalAddress"/>
	</form:label>
	<form:input path="postalAddress"/>
	<form:errors cssClass="error" path="postalAddress"/>
	
	<form:label path="email">
		<spring:message code="admin.email"/>
	</form:label>
	<form:input path="email"/>
	<form:errors cssClass="error" path="email"/>
	
	<form:label path="birthDate">
		<spring:message code="admin.birthDate"/>
	</form:label>
	<form:input path="birthDate" placeholder="dd/MM/yyyy"/>
	<form:errors cssClass="error" path="birthDate"/>
	
	<form:label path="userAccount.username">
		<spring:message code="admin.username"/>
	</form:label>
	<form:input path="userAccount.username"/>
	<form:errors cssClass="error" path="userAccount.username"/>
	<br/>
	
	<form:label path="userAccount.password">
		<spring:message code="admin.password"/>
	</form:label>
	<form:password path="userAccount.password"/>
	<form:errors cssClass="error" path="userAccount.password"/>
	<br/>
	
	<label for = "confirmPassword">
		<spring:message code="admin.confirm.password" />
	</label>
	<input type = "password" name="confirmPassword" />
	<br />
	
	

	<div>
	<input 
		type="submit"
		name="save"
		class = "btn"
		value="<spring:message code="admin.save" />" />
		
	<input 
		type="button"
		name="cancel"
		class = "btn"
		value="<spring:message code="admin.cancel" />"
		onclick="javascript: relativeRedir('user/list.do');" /> 
	</div>

</form:form>
