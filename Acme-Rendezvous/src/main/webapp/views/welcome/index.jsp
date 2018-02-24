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

<div class="slider fullscreen">
    <ul class="slides">
      <li>
        <img src="images/slider-1.jpg">
        <div class="caption left-align">
          <h3 class = "slider-text"><spring:message code = "welcome.slider1.title"/></h3>
          <h5 class="light grey-text text-lighten-3 slider-text"><spring:message code = "welcome.slider1.desc"/></h5>
        </div>
      </li>
      <li>
        <img src="images/slider-2.jpg">
        <div class="caption right-align">
          <h3 class = "slider-text"><spring:message code = "welcome.slider2.title"/></h3>
          <h5 class="light grey-text text-lighten-3 slider-text"><spring:message code = "welcome.slider2.desc"/></h5>
        </div>
      </li>
      <li>
        <img src="images/slider-3.jpg">
        <div class="caption center-align">
          <h3 class = "slider-text"><spring:message code = "welcome.slider3.title"/></h3>
          <h5 class="light grey-text text-lighten-3 slider-text"><spring:message code = "welcome.slider3.desc"/></h5>
        </div>
      </li>
    </ul>
  </div>

