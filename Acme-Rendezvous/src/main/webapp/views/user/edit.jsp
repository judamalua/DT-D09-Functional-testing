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
	<input type="hidden" name="rendezvousId" id="rendezvousId" value="${rendezvousId}"/>
	
	
	<form:label path="text">
		<spring:message code="question.text"/>
	</form:label>
	<form:input path="text"/>
	<form:errors cssClass="error" path="text"/>
	<br/>
	
	
	<input 
		type="submit"
		name="save"
		class = "btn"
		value="<spring:message code="question.save" />" />
		
	<jstl:if test="${question.id!=0}">
		<input 
			type="submit"
			name="delete"
			class = "btn"
			value="<spring:message code="question.delete" />"
			onclick="return confirm('<spring:message code='question.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		class = "btn"
		value="<spring:message code="question.cancel" />"
		onclick="javascript: relativeRedir('rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvousId}');" /> <!-- This variable is sent by the controller -->

</form:form>
