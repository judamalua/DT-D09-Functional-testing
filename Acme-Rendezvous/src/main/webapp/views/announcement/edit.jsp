<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form id = "form" action="announcement/user/edit.do" modelAttribute ="announcement">
	
	<input type="hidden" name="rendezvousId" id="rendezvousId" value="${rendezvousId}"/>
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<p><em><spring:message code = "form.required.params"/></em></p>
	
	<acme:textbox code="announcement.title" path="title"/>
	
	<acme:textarea code="announcement.description" path="description" required = "true"/>
	
	<acme:submit name="save" code="announcement.save"/>
		
	<jstl:if test="${announcement.id!=0}">
		
		<acme:delete clickCode="announcement.confirm.delete" name="delete" code="announcement.delete"/>
		
	</jstl:if>
	
	<acme:cancel url="announcement/list.do" code="announcement.cancel"/>

</form:form>
