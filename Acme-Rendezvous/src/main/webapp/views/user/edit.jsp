<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form id = "form" action="/user/register.do" modelAttribute ="user">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="createdRendezvouses"/>
	<form:hidden path="rsvpRendezvouses"/>
	<form:hidden path="comments"/>
	
	
	<form:label path="name">
		<spring:message code="user.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	
	
	<form:label path="surname">
		<spring:message code="user.surname"/>
	</form:label>
	<form:input path="surname"/>
	<form:errors cssClass="error" path="surname"/>
	
	
	<form:label path="phoneNumber">
		<spring:message code="user.phoneNumber"/>
	</form:label>
	<form:input path="phoneNumber"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	
	<<form:label path="postalAddress">
		<spring:message code="user.postalAddress"/>
	</form:label>
	<form:input path="postalAddress"/>
	<form:errors cssClass="error" path="postalAddress"/>
	
	<form:label path="email">
		<spring:message code="user.email"/>
	</form:label>
	<form:input path="email"/>
	<form:errors cssClass="error" path="email"/>
	
	<form:label path="birthDate">
		<spring:message code="user.birthDate"/>
	</form:label>
	<form:input path="birthDate"/>
	<form:errors cssClass="error" path="birthDate"/>
	

	
	<input 
		type="submit"
		name="save"
		class = "btn"
		value="<spring:message code="user.save" />" />
		
	<input 
		type="button"
		name="cancel"
		class = "btn"
		value="<spring:message code="user.cancel" />"
		onclick="javascript: relativeRedir('user/list.do');" /> 

</form:form>
