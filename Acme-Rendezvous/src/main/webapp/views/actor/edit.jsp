<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Selecting request uri -->
<jstl:set var="requestURI" value="actor/edit-admin.do"/>
<security:authorize access="hasRole('USER')">
	<jstl:set var="requestURI" value="actor/edit-user.do"/>
</security:authorize>


<form:form id = "form" action="${requestURI}" modelAttribute ="actor">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<security:authorize access="hasRole('USER')">
		<form:hidden path="createdRendezvouses"/>
		<form:hidden path="comments"/>
	</security:authorize>
	<form:hidden path="userAccount"/>
	
	<form:label path="name">
		<spring:message code="actor.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	
	
	<form:label path="surname">
		<spring:message code="actor.surname"/>
	</form:label>
	<form:input path="surname"/>
	<form:errors cssClass="error" path="surname"/>
	
	
	<form:label path="phoneNumber">
		<spring:message code="actor.phoneNumber"/>
	</form:label>
	<form:input path="phoneNumber"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	
	<form:label path="postalAddress">
		<spring:message code="actor.postalAddress"/>
	</form:label>
	<form:input path="postalAddress"/>
	<form:errors cssClass="error" path="postalAddress"/>
	
	<form:label path="email">
		<spring:message code="actor.email"/>
	</form:label>
	<form:input path="email"/>
	<form:errors cssClass="error" path="email"/>
	
	<form:label path="birthDate">
		<spring:message code="actor.birthDate"/>
	</form:label>
	<form:input path="birthDate"/>
	<form:errors cssClass="error" path="birthDate"/>
	

	
	<input 
		type="submit"
		name="save"
		class = "btn"
		value="<spring:message code="actor.save" />" />
		
	<input 
		type="button"
		name="cancel"
		class = "btn"
		value="<spring:message code="actor.cancel" />"
		onclick="javascript: relativeRedir('/');" /> 

</form:form>
