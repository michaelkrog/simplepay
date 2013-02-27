<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                            <button class="btn btn-mini btn-primary">Recent Payments</button>
                            <button class="btn btn-mini">All Payments</button>
                        </div>
                        <div class="pull-right"><a href="#paymentModal" role="button" data-toggle="modal">Create payment</a></div>
                        </p>
                        <div>

                            <ul class="nav nav-tabs nav-stacked clear">
                                <c:if test="${empty entities}">
                                    <li><a href="#">No payments</a></li>
                                </c:if>
                                <c:forEach var="e" items="${entities}">
                                    <li>
                                        <a href="<c:url value="/data/transactions/${e.id}.html"/>">
                                            <div class="pull-left" style="width:50%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;padding-right: 15px"><fmt:formatNumber currencyCode="${e.currency}" value="${e.amount/100}" type="currency"/> — ${e.id}</div>
                                            <span>${e.status}</span>

                                            <span class="pull-right">&nbsp;<i class="mini-ico-ok mini-color"></i></span>
                                            <span class="pull-right hidden-phone" style="padding-left:15px"><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${e.dateChanged}" />&nbsp;<i class="mini-ico-circle-arrow-right mini-color"></i></span>
                                            <span class="pull-right visible-phone" style="padding-left:15px"><fmt:formatDate type="date" dateStyle="medium" value="${e.dateChanged}" />&nbsp;<i class="mini-ico-circle-arrow-right mini-color"></i></span>

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

        <!-- Modal -->
        <div id="paymentModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">New Payment</h3>
            </div>
            <form id="form-create-payment" class="form-horizontal">
                
            <div class="modal-body">
                    <div class="control-group">
                        <label class="control-label" for="inputAmount">Amount</label>
                        <div class="controls">
                            <div class="input-append ">
                                <input type="text" pattern="\d+(\.\d{2})?" class="input-small" id="inputAmount" placeholder="<fmt:formatNumber value="9.99"/>" required>
                                <span class="add-on">DKK</span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputCard">Card Number</label>
                        <div class="controls">
                            <input type="text" pattern="^[0-9\s]{13,16}?$" class="input-medium" id="inputCard" placeholder="**** **** **** ****" required>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputDescription">Description</label>
                        <div class="controls">
                            <input type="text" class="input-medium" id="inputDescription" placeholder="">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputCard">Expires</label>
                        <div class="controls">
                            <select id="inputExpireMonth" class="input-mini">
                                <c:forEach var="currentMonth" begin="${1}" end="${12}">
                                    <option>${currentMonth}</option>
                                </c:forEach>

                            </select>

                            <jsp:useBean id="now" class="java.util.Date" scope="request" />
                            <fmt:formatDate var="year" value="${now}" pattern="yyyy" />
                            <select id="inputExpireYear" class="input-small">
                                <c:forEach var="currentYear" begin="${year}" end="${year+11}">
                                    <option>${currentYear}</option>
                                </c:forEach>

                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputCvd">CVC</label>
                        <div class="controls">
                            <input type="text" pattern="^[0-9]{3}?$" class="span1" id="inputCvd" placeholder="***" required>
                        </div>
                    </div>
                
            </div>
            <div class="modal-footer">
                <a href="#" class="btn" data-dismiss="modal" aria-hidden="true">Cancel</a>
                <button type="submit" class="btn btn-primary">Create payment</button>
            </div>
            </form>
        </div>
        <%@include file="inc/footer_menu.jsp" %>
        <%@include file="inc/footer.jsp" %>

        <%@include file="inc/post_body.jsp" %>
        <script src="<c:url value="/api.js"/>"></script>

        <script>
            function onTokenCreated(data) {
                alert(data)
            }
            
            function onTokenFailed(data) {
                alert(data);
            }
            $('#form-create-payment').submit(function() {
               SimplePay.createToken($('#inputCard').val(), $('#inputExpireYear').val(), $('#inputExpireMonth').val(), $('#inputCvd').val(), onTokenCreated, onTokenFailed); 
               return false;
            });
        </script>
    </body>
</html>
<!-- Localized -->
