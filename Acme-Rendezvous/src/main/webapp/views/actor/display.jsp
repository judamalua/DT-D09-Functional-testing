
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Variables -->
<spring:message code="master.page.moment.format" var="formatDate" />
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

<spring:message code="actor.postalAddress" />
:
<jstl:out value="${actor.postalAddress}" />
<br />
<spring:message code="actor.phoneNumber" />
:
<jstl:out value="${actor.phoneNumber}" />
<br />
<spring:message code="actor.email" />
:
<jstl:out value="${actor.email}" />
<br />
<spring:message code="actor.birthDate" />
:
<jstl:out value="${actor.birthDate}" />
<br />

<security:authorize access="hasRole('USER')">

	<!-- Display created Rendezvouses-->

	<!-- Pagination -->
	<jstl:forEach begin="1" end="${createdPageNum}"
		var="createdRendezvousIndex">
		<a
			href="user/display.do?actorId=${actor.id}&anonymous=${anonymous}&rsvpPage=${rsvpPage}&createdRendezvousPage=${createdRendezvousIndex-1}">
			<jstl:out value="${createdRendezvousIndex}" />
		</a>
		<jstl:if test="${createdRendezvousIndex!=rsvpPageNum}">,</jstl:if>
	</jstl:forEach>
	<br />
	<!-- Pagination -->

	<display:table name="${createdRendezvouses}" id="rendezvous"
		requestURI="user/display.do" pagesize="10">

		<display:column property="name" title="${titleName}" />
		<display:column property="description" title="${titleDescription}" />
		<display:column property="moment" title="${titleMoment}"
			format="${formatMoment}" />
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
				<img src="images/deleted-rendezvous.png" />
				<spring:message code="rendezvous.deleted" />
			</display:column>
		</jstl:if>

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

	<!-- Display created Rendezvouses-->


	<!-- Pagination -->
	<jstl:forEach begin="1" end="${rsvpPageNum}" var="rsvpIndex">
		<a
			href="user/display.do?actorId=${actor.id}&anonymous=${anonymous}&rsvpPage=${rsvpIndex-1}&createdRendezvousPage=${createdRendezvousPage}">
			<jstl:out value="${rsvpIndex}" />
		</a>
		<jstl:if test="${rsvpIndex!=rsvpPageNum}">,</jstl:if>
	</jstl:forEach>
	<br />
	<!-- Pagination -->

	<display:table name="${rsvpRendezvouses}" id="rsvpRendezvous"
		requestURI="user/display.do" pagesize="10">

		<display:column property="name" title="${titleName}" />
		<display:column property="description" title="${titleDescription}" />
		<display:column property="moment" title="${titleMoment}"
			format="${formatMoment}" />
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
				<img src="images/deleted-rendezvous.png" />
				<spring:message code="rendezvous.deleted" />
			</display:column>
		</jstl:if>

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
</security:authorize>