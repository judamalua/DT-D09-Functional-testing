
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Variables -->
<spring:message code="master.page.moment.format" var="formatDate" />
<spring:message var="format" code="master.page.moment.format.out" />
<spring:message var="birthDateFormat" code="master.page.birthDate" />
<spring:message code="rendezvous.name" var="titleName" />
<spring:message code="rendezvous.description" var="titleDescription" />
<spring:message code="rendezvous.moment" var="titleMoment" />

<!-- Personal data -->
<br />
<h4>
	<jstl:out value="${actor.name}" />
	<jstl:out value="${actor.surname}" />
</h4>
<br />

<strong><spring:message code="actor.postalAddress" />:</strong>
<jstl:out value="${actor.postalAddress}" />
<br />
<strong><spring:message code="actor.phoneNumber" />:</strong>
<jstl:out value="${actor.phoneNumber}" />
<br />
<strong><spring:message code="actor.email" />:</strong>
<jstl:out value="${actor.email}" />
<br />
<strong><spring:message code="actor.birthDate" />:</strong>
<fmt:formatDate value="${actor.birthDate}" pattern="${birthDateFormat}" />
<br />
<br />

<!-- Display created Rendezvouses-->
<jstl:if test="${createdRendezvouses!=null}">
	<h4>
		<spring:message code="actor.createdRendezvouses" />
	</h4>

	
	<!-- Pagination -->
	<acme:pagination pageNum="${createdPageNum}" requestURI="user/display.do?actorId=${actor.id}&anonymous=${anonymous}&rsvpPage=${rsvpPage}&createdRendezvousPage=" page = "${createdRendezvousPage}"/>

	<display:table name="${createdRendezvouses}" id="rendezvous"
		requestURI="user/display.do">

		<display:column property="name" title="${titleName}" sortable="true" />
		<display:column property="description" title="${titleDescription}" />
		<display:column property="moment" title="${titleMoment}"
			format="${formatDate}" sortable="true" />
		<display:column>
			<a
				href="rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvous.id}&anonymous=${anonymous}">
				<button class="btn">
					<spring:message code="rendezvous.details" />
				</button>
			</a>
		</display:column>

		<jstl:if test="${rendezvous.deleted}">
			<display:column>
				<i class="material-icons">delete</i>
				<spring:message code="rendezvous.deleted" />
			</display:column>
		</jstl:if>

		<display:column>
			<jstl:if test="${rendezvous.adultOnly}">
				<i class="material-icons">do_not_disturb</i>
				<spring:message code="rendezvous.adultOnly" />
			</jstl:if>
		</display:column>

		<security:authorize access="hasRole('ADMIN')">
			<display:column>
				<a href="rendezvous/admin/delete.do?rendezvousId=${rendezvous.id}">
					<button class="btn">
						<spring:message code="rendezvous.delete" />
					</button>
				</a>
			</display:column>
		</security:authorize>

	</display:table>
	<br />
</jstl:if>

<jstl:if test="${rsvpRendezvouses!=null}">
	<!-- Display RSVP Rendezvouses-->
	<h4>
		<spring:message code="actor.rsvpRendezvouses" />
	</h4>

	<!-- Pagination -->
	
	<acme:pagination pageNum="${rsvpPageNum}" requestURI="user/display.do?actorId=${actor.id}&anonymous=${anonymous}&createdRendezvousPage=${createdRendezvousPage}&rsvpPage=" page = "${rsvpPage}"/>

	<display:table name="${rsvpRendezvouses}" id="rsvpRendezvous"
		requestURI="user/display.do">

		<display:column property="name" title="${titleName}" sortable="true" />
		<display:column property="description" title="${titleDescription}"
			sortable="true" />
		<display:column property="moment" title="${titleMoment}"
			format="${formatDate}" />
		<display:column>
			<a
				href="rendezvous/detailed-rendezvous.do?rendezvousId=${rsvpRendezvous.id}&anonymous=${anonymous}">
				<button class="btn">
					<spring:message code="rendezvous.details" />
				</button>
			</a>
		</display:column>



		<jstl:if test="${rsvpRendezvous.deleted}">
			<display:column>
				<i class="material-icons">delete</i>
				<spring:message code="rendezvous.deleted" />
			</display:column>
		</jstl:if>

		<display:column>
			<jstl:if test="${rsvpRendezvous.adultOnly}">
				<i class="material-icons">do_not_disturb</i>
				<spring:message code="rendezvous.adultOnly" />
			</jstl:if>
		</display:column>



		<security:authorize access="hasRole('ADMIN')">
			<display:column>
				<a
					href="rendezvous/admin/delete.do?rendezvousId=${rsvpRendezvous.id}">
					<button class="btn">
						<spring:message code="rendezvous.delete" />
					</button>
				</a>
			</display:column>
		</security:authorize>

	</display:table>
</jstl:if>
