<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="area" value="dashboard"/>
<c:set var="subarea" value="events"/>
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

                    <h2><i class="ico-credit-card ico-white"></i>Events</h2>

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
                    <div class="span3">
                        <div data-spy="affix" data-offset-top="0">
                            <%@include file="inc/menu-left.jsp" %>
                        </div>
                    </div>
                    <div class="span9">
                        <div>
                            <p>

                            <ul class="nav nav-tabs nav-stacked clear">
                                <c:if test="${empty entities}">
                                    <li><a href="#"><spring:message code="transactions.no_payments"/></a></li>
                                    </c:if>
                                    <c:forEach var="e" items="${entities}">
                                    <li>
                                        <a href="<c:url value="/manage/transactions/${e.id}"/>">
                                            <div class="pull-left hidden-phone" style="width:25%">
                                                <span><fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${e.timestamp}" /></span>
                                            </div>
                                            &nbsp;

                                            <div class="pull-left " style="width:70%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;padding-right: 15px">
                                                <c:choose>
                                                    <c:when test="${e.type=='tokenEvent'}">
                                                        Token was created for ${e.token.data.cardNumber} (${e.token.data.paymentInstrument})
                                                    </c:when>
                                                    <c:when test="${e.type=='transactionEvent'}">
                                                        <c:choose>
                                                            <c:when test="${e.newStatus=='Authorized'}">
                                                                A transaction was authorized for <fmt:formatNumber value="${e.transaction.amount}" type="currency" currencyCode="${e.transaction.currency}"/>
                                                            </c:when>
                                                            <c:when test="${e.newStatus=='Charged'}">
                                                                A transaction was charged for <fmt:formatNumber value="${e.transaction.amountCharged}" type="currency" currencyCode="${e.transaction.currency}"/>
                                                            </c:when>
                                                            <c:when test="${e.newStatus=='Refunded'}">
                                                                A transaction was refunded <fmt:formatNumber value="${e.transaction.amountRefunded}" type="currency" currencyCode="${e.transaction.currency}"/>
                                                            </c:when>
                                                            <c:when test="${e.newStatus=='Cancelled'}">
                                                                A transaction was cancelled.
                                                            </c:when>
                                                        </c:choose>
                                                    </c:when>
                                                </c:choose>
                                            </div>
                                            
                                            

                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </p>
                            <div id="btn-approve-charges" class="btn btn-small pull-right disabled"><spring:message code="transactions.approve_charges"/></div>
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
                <h3 id="myModalLabel"><spring:message code="transactions.new_payment"/></h3>
            </div>
            <form id="form-create-payment" class="form-horizontal">

                <div class="modal-body">
                    <div class="control-group">
                        <label class="control-label" for="inputAmount"><spring:message code="general.amount"/></label>
                        <div class="controls">
                            <div class="input-append ">
                                <input type="text" title="<spring:message code="general.formats.amount.description"/>" pattern="<spring:message code="general.formats.amount.js"/>" class="input-small" id="inputAmount" placeholder="<spring:message code="transactions.amount.placeholder"/>" required>
                                <span class="add-on">DKK</span>
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputCard"><spring:message code="general.card_number"/></label>
                        <div class="controls">
                            <input type="text" title="<spring:message code="general.formats.card_number.description"/>" pattern="<spring:message code="general.formats.card_number.js"/>" class="input-medium" id="inputCard" placeholder="<spring:message code="transactions.card_number.placeholder"/>" required>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputDescription"><spring:message code="general.reference"/></label>
                        <div class="controls">
                            <input type="text" class="input-medium" id="inputReference" placeholder="<spring:message code="transactions.reference.placeholder"/>">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputCard"><spring:message code="general.expires"/></label>
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
                        <label class="control-label" for="inputCvd"><spring:message code="general.cvd"/></label>
                        <div class="controls">
                            <input title="<spring:message code="general.formats.cvd.description"/>" type="text" pattern="<spring:message code="general.formats.cvd.js"/>" class="span1" id="inputCvd" placeholder="<spring:message code="transactions.cvd.placeholder"/>" required>
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <a href="#" class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="general.cancel"/></a>
                    <button type="submit" class="btn btn-primary"><spring:message code="transactions.create_payment"/></button>
                </div>
            </form>
        </div>
        <%@include file="inc/footer_menu.jsp" %>
        <%@include file="inc/footer.jsp" %>

        <%@include file="inc/post_body.jsp" %>
        <script src="<c:url value="/api.js"/>"></script>

        <script>
            var selectedCharges = new Array();
            var index = 0;
            var approveEnabled = false;
            var decimalDelimiter = '<spring:message code="general.formats.decimal_delimiter.js"/>';
            
            SimplePay.setKey('${key}');
            
            function setApproveButtonEnabled(value) {
                approveEnabled = value === true;
                if(approveEnabled) {
                    $('#btn-approve-charges').removeClass('disabled');
                } else{
                    $('#btn-approve-charges').addClass('disabled');
                }
                
            }
            
            function onTransactionCreated(transaction) {
                document.location.reload();
            }

            function onTokenCreated(token) {
                var amount = $('#inputAmount').val();
                if(decimalDelimiter != '.') {
                    amount = amount.replace(decimalDelimiter,'.');
                }
                SimplePay.createTransaction(token.id, Math.round(amount * 100), "DKK", $('#inputReference').val(), onTransactionCreated, onTransactionFailed);
            }

            function onTokenFailed(xhr, ajaxOptions, thrownError) {
                alert("Error: " + thrownError);
            }

            function onTransactionFailed(xhr, ajaxOptions, thrownError) {
                alert("Error: " + thrownError);
            }

            $('#form-create-payment').submit(function() {
                SimplePay.createToken($('#inputCard').val(), $('#inputExpireYear').val(), $('#inputExpireMonth').val(), $('#inputCvd').val(), onTokenCreated, onTokenFailed);
                return false;
            });
            
            $('button[data-type="charge"]').click(function(e) {
                var id = $(this).attr('data-id');
                var parent = $(this).parent().parent();
                
                if($(this).hasClass('active')) {
                    $(parent).addClass('authorized-transaction');
                    $(parent).removeClass('selected');
                    
                    var index = $.inArray(id, selectedCharges)
                    if(index >= 0) {
                        //remove id from list
                        selectedCharges.splice(index, 1);
                    }
                } else {
                    $(parent).removeClass('authorized-transaction');
                    $(parent).addClass('selected');
                    
                    if($.inArray(id, selectedCharges) < 0) {
                        //add id to list
                        selectedCharges[selectedCharges.length] = id;
                    }
                    
                    
                }
                
                setApproveButtonEnabled(selectedCharges.length > 0);
                
                e.preventDefault();
            });
            
            $('#btn-approve-charges').click(function() {
                index = 0;
                doNextCharge();
            });
            
            function doNextCharge() {
                if(index<selectedCharges.length) {
                    SimplePay.chargeTransaction(selectedCharges[index],null, onSuccessfullCharge, onFailedCharge);
                } else {
                    document.location.reload();
                }
            }
            
            function onSuccessfullCharge() {
                index++;
                doNextCharge();
            }
            
            function onFailedCharge() {
                index++;
                doNextCharge();
            }
            
            setApproveButtonEnabled(false);
        </script>
    </body>
</html>
<!-- Localized -->
