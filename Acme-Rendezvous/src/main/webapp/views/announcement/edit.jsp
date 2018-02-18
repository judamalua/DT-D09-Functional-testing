<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form id = "form" action="announcement/user/edit.do" modelAttribute ="announcement">
	
	<input type="hidden" name="rendezvousId" id="rendezvousId" value="${rendezvousId}"/>
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>
	
	
	
	<form:label path="title">
		<spring:message code="announcement.title"/>
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="announcement.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	
	<input 
		type="submit"
		name="save"
		class = "btn"
		value="<spring:message code="announcement.save" />" />
		
	<jstl:if test="${announcement.id!=0}">
		<input 
			type="submit"
			name="delete"
			class = "btn"
			value="<spring:message code="announcement.delete" />"
			onclick="return confirm('<spring:message code='announcement.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		class = "btn"
		value="<spring:message code="announcement.cancel" />"
		onclick="javascript: relativeRedir('announcement/list.do;" /> <!-- This variable is sent by the controller -->

</form:form>
