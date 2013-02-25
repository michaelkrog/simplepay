<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!--start: Navbar -->
<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <a class="brand" href="index.html"><i class="ico-cloud circle"></i>Simple<span>Pay</span>.</a>
        <div class="nav-collapse collapse">
            <ul class="nav">
                <li class="active"><a href="/" class="dropdown-toggle"><spring:message code="navigation.frontpage"/></a></li>
                <li><a href="about.html"><spring:message code="navigation.about_us"/></a></li>
                <!--li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Funktioner<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="social-icons.html">Social Icons</a></li>
                        <li><a href="icons.html">Icons</a></li>
                        <li><a href="sliders.html">Sliders</a></li>
                        <li><a href="typography.html">Typography</a></li>
                        <li><a href="shortcodes.html">Shortcodes</a></li>
                        <li><a href="list-styles.html">List Styles</a></li>
                    </ul>
                </li-->
                <!--li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Portefølje<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="portfolio3.html">3 Columns</a></li>
                        <li><a href="portfolio4.html">4 Columns</a></li>
                    </ul>
                </li-->									
                <!--li><a href="services.html">Services</a></li-->
                <li><a href="pricing.html"><spring:message code="navigation.prices"/></a></li>
                <!--li><a href="blog.html">Blog</a></li-->
                <li><a href="contact.html"><spring:message code="navigation.contact"/></a></li>
                <li class="divider-vertical"></li>
                <sec:authorize ifNotGranted="ROLE_MERCHANT">
                <li><a href="<c:url value="/login.html"/>"><spring:message code="navigation.log_in"/></a></li>
                </sec:authorize>
                <sec:authorize ifAnyGranted="ROLE_MERCHANT">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Mit SimplePay&nbsp;<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value="/account"/>">Min konto</a></li>
                        <li><a href="<c:url value="/data/transactions.html"/>">Oversigt</a></li>
                        <li><a href="<c:url value="/logout"/>">Log ud</a></li>
                    </ul>
                </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</div>
<!--end: Navbar -->
