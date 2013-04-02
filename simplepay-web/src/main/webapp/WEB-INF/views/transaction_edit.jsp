<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="area" value="dashboard"/>
<c:set var="subarea" value="payments"/>
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

                    <h2><i class="ico-credit-card ico-white"></i>Payments</h2>

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
                        <%@include file="inc/menu-left.jsp" %>
                    </div>
                    <div class="span9">
                        <c:choose>
                            <c:when test="${entity.status=='Authorized'}">
                                <div class="btn-group pull-right">
                                <div id="btn-charge" class="btn btn-success">Charge Payment</div>
                                <div id="btn-cancel" class="btn btn-inverse">Cancel Payment</div>
                                </div>
                            </c:when>
                            <c:when test="${entity.status=='Charged'}"><div id="btn-refund" class="btn btn-danger  pull-right">Refund Payment</div></c:when>
                        </c:choose>
                        

                        <h1 class="pull-left">
                            <img src="<c:url value='/img/cards/48/${fn:toLowerCase(token.data.paymentInstrument)}_48.png'/>">
                            <fmt:formatNumber value="${entity.amount/100}" type="currency" currencyCode="${entity.currency}"/>
                        </h1>
                        <hr class="clear"/>
                            
                        <h3>Payment Details</h3>
                        <div class="well">
                            <dl class="dl-horizontal">
                                <dt>Amount:</dt>
                                <dd><fmt:formatNumber value="${entity.amount/100}" type="currency" currencyCode="${entity.currency}"/></dd>
                                <dt>Id:</dt>
                                <dd>${entity.id}</dd>
                                <dt>Date:</dt>
                                <dd><fmt:formatDate value="${entity.dateCreated}" type="both"/></dd>
                                <dt>Status:</dt>
                                <dd>${entity.status}</dd>
                                
                            </dl>
                        </div>
                                
                        <h3>Card Details</h3>
                        <div class="well">
                            <dl class="dl-horizontal">
                                <dt>Number:</dt>
                                <dd>${token.data.cardNumber}</dd>
                                <dt>Expires:</dt>
                                <dd>${token.data.expireMonth} / ${token.data.expireYear}</dd>
                                <dt>Type</dt>
                                <dd>${token.data.paymentInstrument}</dd>
                                
                            </dl>
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
        <script src="<c:url value="/api.js"/>"></script>

        <script>
            SimplePay.setKey('${key}');
        
            $('#btn-charge').click(function() {
                SimplePay.chargeTransaction("${entity.id}", null, onCharge, onChargeFailed);
            });
            
            $('#btn-refund').click(function() {
                if(confirm("Are you sure you want to refund this payment?")) {
                    SimplePay.refundTransaction("${entity.id}", null, onRefund, onRefundFailed);
                }
            });
            
            $('#btn-cancel').click(function() {
                if(confirm("Are you sure you want to cancel this payment?")) {
                    SimplePay.cancelTransaction("${entity.id}", onRefund, onRefundFailed);
                }
            });
            
            function onCharge() {
                document.location.reload();
            }
            
            function onChargeFailed() {
                alert("fail");
            }
            
            function onRefund() {
                document.location.reload();
            }
            
            function onRefundFailed() {
                alert("fail");
            }
            
            function onCancel() {
                document.location.reload();
            }
            
            function onCancelFailed() {
                alert("fail");
            }
        </script>

    </body>
</html>
<!-- Localized -->