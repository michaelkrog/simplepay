<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
/* 
 * Copyright by Apaq 2011-2013
 */
(function(){
    var w = this;
    this.SimplePay = new function() {
    this.serviceUrl = '${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI, pageContext.request.contextPath)}/data';
    this.serviceKey = '';
    
        this.setKey = function(key) {
            this.serviceKey = key;
        }
    
        this.createToken = function(cardNumber, expireYear, expireMonth, cvd, callbackSuccess, callbackFailure) {
            var data = {
                cardNumber:cardNumber,
                expireYear:expireYear,
                expireMonth:expireMonth,
                cvd:cvd
            }
            $.ajax({
                type: "POST",
                url: this.serviceUrl + '/tokens',
                data: data,
                dataType: 'json',
                username: this.serviceKey
              }).done(callbackSuccess).fail(callbackFailure);
        }
        
        
        return this;
    }
    
    $('form[data-type="payment"]').submit(function() {
        var thatform = this;
        try {
            var key = $(this).attr('data-key');
            var card = $(this).find('[data-pay="cardno"]').val();
            var cvd = $(this).find('[data-pay="cvd"]').val();
            var expMonth = $(this).find('[data-pay="expireMonth"]').val();
            var expYear = $(this).find('[data-pay="expireYear"]').val();

            SimplePay.setKey(key);
            SimplePay.createToken(card, expYear, expMonth, cvd, function(token) {
                $(thatform).append('<input type="hidden" name="token" value='+token.id+'>');
                alert(token)
                
            }, function(err) {
                
                alert("Error: " + err);
            });
        } finally {
            return false;
        }
    });
    
    return this;
}).call(this);
