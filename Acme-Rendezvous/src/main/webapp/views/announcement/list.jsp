<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:set var = "rendezvousNameView" value = "${rendezvousName}"/> <!-- rendezvousName is passed by controller, obtained by a query by means of the id of the Rendezvous-->

<h3><spring:message code = "announcement.rendezvous"/> ${rendezvousNameView}</h3>

<display:table name="announcements" id="announcement" requestURI="announcement/user/list.do?rendezvousId=${rendezvousId}" pagesize="${pagesize}" class="displayTag"> <!-- Rendezvous Id sent by controller -->
	
	<spring:message code="announcement.text" var="text"/>
	<display:column property="text" title="${text}" sortable="false"/>
	
	<display:column>
		<a href="announcement/user/edit.do?announcementId=${announcement.id}">
			<spring:message code="announcement.edit"/>
		</a>
	</display:column>
	
</display:table>

	<a href = "announcement/user/create.do?rendezvousId=${rendezvousId}"> <!-- Rendezvous Id sent by controller -->
	<button class = "btn">
		<spring:message code = "announcement.create"/>
	</button>
	</a>


