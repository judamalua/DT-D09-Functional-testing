<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:set var = "rendezvousNameView" value = "${rendezvousName}"/> <!-- rendezvousName is passed by controller, obtained by a query by means of the id of the Rendezvous-->

<h3><spring:message code = "question.rendezvous"/> ${rendezvousNameView}</h3>

<display:table name="questions" id="question" requestURI="question/user/list.do?rendezvousId=${rendezvousId}" pagesize="${pagesize}" class="displayTag"> <!-- Rendezvous Id sent by controller -->
	
	<spring:message code="question.text" var="text"/>
	<display:column property="text" title="${text}" sortable="false"/>
	
	<display:column>
		<a href="question/user/edit.do?questionId=${question.id}">
			<spring:message code="question.edit"/>
		</a>
	</display:column>
	
</display:table>

<jstl:if test = "${userHasCreatedRendezvous}"> <!-- Variable sent by the controller to see if the principal is the creator of this Rendezvous -->
	<a href = "question/user/create.do?rendezvousId=${rendezvousId}"> <!-- Rendezvous Id sent by controller -->
	<button class = "btn">
		<spring:message code = "question.create"/>
	</button>
	</a>
</jstl:if>

