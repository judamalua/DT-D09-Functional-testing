<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p>
	<strong><spring:message code="configuration.cookies_eng" /></strong>
	<jstl:out value="${configuration.cookies_eng}" />
</p>

<p>
	<strong><spring:message code="configuration.cookies_es" /></strong>
	<jstl:out value="${configuration.cookies_es}" />
</p>

<p>
<strong><spring:message code="configuration.businessName" /></strong>:
&#160;&#160;<jstl:out value="${configuration.businessNameFirst}" />&#160;<img width="24" src="favicon.ico" />&#160;<jstl:out value="${configuration.businessNameLast}" />
</p>

<p>
	<strong><spring:message code="configuration.pagesize" /></strong>
	<jstl:out value="${configuration.pageSize}" />
</p>
<acme:button url="slider/admin/list.do" code="configuration.slider"/>
<br/>
<acme:button url="configuration/admin/edit.do" code="configuration.edit"/>
