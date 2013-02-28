<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
/* 
 * Copyright by Apaq 2011-2013
 */
(function(){
    var w = this;
    this.SimplePay = new function() {
    this.serviceUrl = '<c:url value="/data"/>';
    
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
                dataType: 'json'
              }).done(callbackSuccess).fail(callbackFailure);
            //$.post(this.serviceUrl + '/tokens', data, "json").done(callbackSuccess).fail(callbackFailure);
        }
        
        this.createTransaction = function(token, amount, currency, refId, callbackSuccess, callbackFailure) {
            var data = {
                token:token,
                amount:amount,
                currency:currency,
                refId:refId
            }
            $.ajax({
                type: "POST",
                url: this.serviceUrl + '/transactions',
                data: data,
                dataType: 'json'
              }).done(callbackSuccess).fail(callbackFailure);
        }
        
        this.listTransactions = function(query, offset, count, callback) {
            $.getJSON(this.serviceUrl + "/transactions?query="+query+"&offset="+offset+"&count=" + count, function(data) {
                callback(data);
            });
        }
        return this;
    }
    return this;
}).call(this);
