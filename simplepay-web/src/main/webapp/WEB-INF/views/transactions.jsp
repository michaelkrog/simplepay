<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
                        <p>
                        <div class="btn-group">
                            <button class="btn btn-mini btn-primary">Recent Payments</button>
                            <button class="btn btn-mini">All Payments</button>
                        </div>
                        <div class="pull-right"><a href="#paymentModal" role="button" data-toggle="modal">Create payment</a></div>
                        </p>
                        <div>
                            <p>

                            <ul class="nav nav-tabs nav-stacked clear">
                                <c:if test="${empty entities}">
                                    <li><a href="#">No payments</a></li>
                                    </c:if>
                                    <c:forEach var="e" items="${entities}">
                                    <li>
                                        <a class="${fn:toLowerCase(e.status)}-transaction" href="<c:url value="/data/transactions/${e.id}.html"/>">
                                            <div class="pull-left hidden-phone" style="width:25%">
                                                <span><fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${e.dateChanged}" /></span>
                                                <!--span class="visible-phone"><fmt:formatDate type="date" dateStyle="short" value="${e.dateChanged}" /></span-->
                                            </div>
                                            &nbsp;

                                            <div class="pull-left visible-desktop" style="width:30%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;padding-right: 15px">
                                                <fmt:formatNumber currencyCode="${e.currency}" value="${e.amount/100}" type="currency"/> — ${e.id}
                                            </div>
                                            <div class="pull-left hidden-desktop" style="width:25%;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;padding-right: 15px">
                                                <fmt:formatNumber currencyCode="${e.currency}" value="${e.amount/100}" type="currency"/>
                                            </div>

                                            <c:choose>
                                                <c:when test="${e.status == 'Authorized'}">
                                                    <div class="btn-group pull-right" style="border-left:1px solid #9f9f9f;margin-left: 10px;padding-left: 10px">
                                                        <button type="button" data-id="${e.id}" data-type="charge" class="btn btn-mini" data-toggle="button" onclick="">Charge&nbsp;<i class="mini-ico-ok mini-color"></i></button>
                                                    </div>
                                                </c:when>
                                                
                                            </c:choose>
                                            <div class="pull-right">#${e.refId}</div>


                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </p>
                            <div id="btn-approve-charges" class="btn btn-small pull-right disabled">Approve Charges</div>
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
                        <label class="control-label" for="inputDescription">Reference</label>
                        <div class="controls">
                            <input type="text" class="input-medium" id="inputReference" placeholder="Reference fx. order id">
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
            var selectedCharges = new Array();
            var index = 0;
            var approveEnabled = false;
            
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
                SimplePay.createTransaction(token.id, Math.round($('#inputAmount').val() * 100), "DKK", $('#inputReference').val(), onTransactionCreated, onTransactionFailed);
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
