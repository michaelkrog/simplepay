/* 
 * Copyright by Apaq 2011-2013
 */
(function(){
    var w = this;
    this.SimplePay = new function() {
        this.serviceUrl = null;
    
        this.setServiceUrl = function(url) {
            this.serviceUrl = url;
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
