<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form id = "form" action="question/user/edit.do" modelAttribute ="question">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="hidden" name="rendezvousId" id="rendezvousId" value="${rendezvousId}"/>
	
	<acme:textbox code="question.text" path="text"/>
	
	<acme:submit name="save" code="question.save"/>
		
	<jstl:if test="${question.id!=0}">
		<acme:delete clickCode="question.confirm.delete" name="delete" code="question.delete"/>
	</jstl:if>
	<acme:cancel url="question/user/list.do?rendezvousId=${rendezvousId}" code="question.cancel"/>
</form:form>
