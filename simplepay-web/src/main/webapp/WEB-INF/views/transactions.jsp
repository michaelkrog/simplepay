<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

                    <h2><i class="ico-italic ico-white"></i>Payments</h2>

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
                        <div class="widget">
                            <div class="title"><h3>Menu</h3></div>
                            <ul class="links-list-alt">
                                <li class="active"><a href="full_width.html">Dashboard</a></li>
                                <li class="active"><a href="full_width.html">Payments</a></li>
                                <li><a href="sidebar.html">Events</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="span9">
                        <p>
                        <div class="btn-group">
                            <button class="btn btn-primary">Recent Payments</button>
                            <button class="btn">All Payments</button>
                        </div>
                        </p>
                        <div>
                            <ul class="nav nav-tabs nav-stacked">
                            <c:forEach var="e" items="${entities}">
                                <li>
                                <a href="<c:url value="/data/transactions/${e.id}.html"/>">
                                    <div class="pull-left" style="width:35%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;padding-right: 15px">adsasd asd asd asd sad asd as d ad asd as das${e.refId}</div>
                                    <span>${e.status}</span>
                                    <span class="pull-right hidden-phone" style="padding-left:15px"><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${e.dateChanged}" />&nbsp;<i class="mini-ico-circle-arrow-right mini-color"></i></span>
                                    <span class="pull-right visible-phone" style="padding-left:15px"><fmt:formatDate type="date" dateStyle="medium" value="${e.dateChanged}" />&nbsp;<i class="mini-ico-circle-arrow-right mini-color"></i></span>
                                    <span class="pull-right" style="text-align:right"><fmt:formatNumber currencyCode="${e.currency}" value="${e.amount/100}" type="currency"/></span>
                                    
                                </a>
                                </li>
                                </c:forEach>
                                </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--end: Container-->

        </div>
        <!-- end: Wrapper  -->			

        <%@include file="inc/footer_menu.jsp" %>
        <%@include file="inc/footer.jsp" %>

        <%@include file="inc/post_body.jsp" %>

    </body>
</html>
<!-- Localized -->