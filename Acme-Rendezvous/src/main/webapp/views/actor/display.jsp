
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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

<h4>
	<spring:message code="actor.createdRendezvouses" />
</h4>

<jstl:if test="${createdPageNum!=0}">
	<!-- Pagination -->
	<span class="pagebanner"> <jstl:forEach begin="1"
			end="${createdPageNum}" var="createdRendezvousIndex">
			<a
				href="user/display.do?actorId=${actor.id}&anonymous=${anonymous}&rsvpPage=${rsvpPage}&createdRendezvousPage=${createdRendezvousIndex-1}">
				<jstl:out value="${createdRendezvousIndex}" />
			</a>
			<jstl:if test="${createdRendezvousIndex!=rsvpPageNum}">,</jstl:if>
		</jstl:forEach>
	</span>
</jstl:if>
<!-- Pagination -->

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
			<img src="images/deleted-rendezvous.png" />
			<spring:message code="rendezvous.deleted" />
		</display:column>
	</jstl:if>

	<display:column>
		<jstl:if test="${rendezvous.adultOnly}">
			<img src="images/18.png" />
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

<!-- Display created Rendezvouses-->
<h4>
	<spring:message code="actor.rsvpRendezvouses" />
</h4>

<jstl:if test="${rsvpPageNum!=0}">
	<!-- Pagination -->
	<span class="pagebanner"> <jstl:forEach begin="1"
			end="${rsvpPageNum}" var="rsvpIndex">
			<a
				href="user/display.do?actorId=${actor.id}&anonymous=${anonymous}&rsvpPage=${rsvpIndex-1}&createdRendezvousPage=${createdRendezvousPage}">
				<jstl:out value="${rsvpIndex}" />
			</a>
			<jstl:if test="${rsvpIndex!=rsvpPageNum}">,</jstl:if>
		</jstl:forEach> <br />
	</span>
</jstl:if>
<!-- Pagination -->

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
			<img src="images/deleted-rendezvous.png" />
			<spring:message code="rendezvous.deleted" />
		</display:column>
	</jstl:if>

	<display:column>
		<jstl:if test="${rsvpRendezvous.adultOnly}">
			<img src="images/18.png" />
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
