<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h3><strong><spring:message code="commercialRegister.company" /></strong></h3>
<strong><spring:message code="commercialRegister.name1" /></strong><spring:message code="commercialRegister.name2" /><br/>
<strong><spring:message code="commercialRegister.headquarters1" /></strong><spring:message code="commercialRegister.headquarters2" /><br/>
<strong><spring:message code="commercialRegister.cif1" /></strong><spring:message code="commercialRegister.cif2" /><br/>
<strong><spring:message code="commercialRegister.phone1" /></strong><spring:message code="commercialRegister.phone2" /><br/>
<strong><spring:message code="commercialRegister.email1" /></strong><spring:message code="commercialRegister.email2" /><br/>