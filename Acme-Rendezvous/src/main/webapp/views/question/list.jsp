<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="questions" id="question" requestURI="question/user/list.do?rendezvousId=${rendezvousId}" pagesize="${pagesize}" class="displayTag"> <!-- Rendezvous Id sent by controller -->
	
	<spring:message code="question.text" var="text"/>
	<display:column property="text" title="${text}" sortable="false"/>
	
	<display:column>
		<a href="question/user/edit.do?questionId=${question.id}">
			<spring:message code="question.edit"/>
		</a>
	</display:column>
	
</display:table>


	<a href = "question/user/create.do?rendezvousId=${rendezvousId}"> <!-- Rendezvous Id sent by controller -->
	<button class = "btn">
		<spring:message code = "question.create"/>
	</button>
	</a>

