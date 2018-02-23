
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<spring:message code="master.page.moment.format" var="formatDate" />
<spring:message code="master.page.birthDate.format" var="formatBirthDate" />

<!-- Pagination -->
<%-- <span class="pagebanner"> 
	<jstl:forEach begin="1" end="${pageNum}" var="index">
		<a href="user/list.do?anonymous=${anonymous}&page=${index-1}">
			<jstl:out value="${index}" />
		</a>
		<jstl:if test="${index!=pageNum}">,</jstl:if>
	</jstl:forEach>
	<br />
</span>

 --%>
<!-- Pagination -->

<acme:pagination requestURI = "user/list.do?anonymous=${anonymous}&page=" pageNum = "${pageNum}" page = "${page}"/>

<display:table name="users" id="row" requestURI="user/list.do"
	class="displaytag">

	<spring:message code="user.name" var="name" />
	<display:column property="name" title="${name}" sortable="true" />

	<spring:message code="user.surname" var="surname" />
	<display:column property="surname" title="${surname}" sortable="true" />

	<spring:message code="user.postalAddress" var="postalAddress" />
	<display:column property="postalAddress" title="${postalAddress}"
		sortable="true" />

	<spring:message code="user.phoneNumber" var="phoneNumber" />
	<display:column property="phoneNumber" title="${phoneNumber}"
		sortable="false" />

	<spring:message code="user.email" var="email" />
	<display:column property="email" title="${email}" sortable="false" />

	<spring:message code="user.birthDate" var="birthDate" />
	<display:column property="birthDate" title="${birthDate}"
		sortable="true" format="${formatBirthDate}" />

	<display:column>
		<a href="user/display.do?actorId=${row.id}&anonymous=${anonymous}">
			<button class="btn">
				<spring:message code="user.display" />
			</button>
		</a>
	</display:column>

</display:table>
