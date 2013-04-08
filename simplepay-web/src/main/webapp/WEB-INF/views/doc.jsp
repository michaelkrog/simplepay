<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="area" value="dashboard"/>
<c:set var="subarea" value="payments"/>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>SimplePay</title>
        <%@include file="inc/head.jsp" %>
    </head>
    <body>

        <!--start: Header -->
        <header>

            <!--start: Container -->
            <div class="container">
                <%@include file="inc/navigation.jsp" %>
            </div>
            <!--end: Container-->			

        </header>
        <!--end: Header-->

        <!-- start: Page Title -->
        <div id="page-title">

            <div id="page-title-inner">

                <!-- start: Container -->
                <div class="container">

                    <h2><i class="ico-credit-card ico-white"></i>Documentation</h2>

                </div>
                <!-- end: Container  -->

            </div>	

        </div>
        <!-- end: Page Title -->

        <!--start: Wrapper-->
        <div id="wrapper">

            <!--start: Container -->
            <div class="container">
                <div class="row">
                    <div class="span3" data-spy="affix" data-offset-top="200">
                        <%@include file="docs/menu.jsp" %>
                    </div>
                    <div class="span9">
                        <textarea id="content-markdown"><jsp:include page="docs/${docfile}_${language}.jsp" /></textarea>
                    </div>
                </div>
            </div>

            <!--end: Container-->

        </div>
        <!-- end: Wrapper  -->			

        <%@include file="inc/footer_menu.jsp" %>
        <%@include file="inc/footer.jsp" %>

        <%@include file="inc/post_body.jsp" %>
        <script src="<c:url value="/js/showdown.js"/>"></script>
        <script>
            var converter = new Showdown.converter();
            $('#content-markdown').replaceWith(converter.makeHtml($('#content-markdown').text()));
            $('#menu-markdown').replaceWith(converter.makeHtml($('#menu-markdown').text()));
            
        </script>
    </body>
</html>
<!-- Localized -->
