function PayService (){
    var publicKey;
    var serviceRoot = "${publicUrl}";
    var amount;
    var refId;
    var key;
    var currency;
    
    this.init = function() {
        var e = $("form[data-type=payment]");
        if(e != null) {
            this.amount = e.attr('data-amount');
            this.refId = e.attr('data-orderref');
            this.publicKey = e.attr('data-key');
            this.currency = e.attr('data-currency');
            e.on("submit", this.handleSubmit);
        }
    }
    
    this.handleSubmit = function() {
        var form = this;
        var cardno = $(form).find('input[name=cardno]').val();
        var cvd = $(form).find('input[name=cvd]').val();
        var expireYear = $(form).find('input[name=expireYear]').val();
        var expireMonth = $(form).find('input[name=expireMonth]').val();
            
        Pay.createToken(null, cardno, cvd, expireMonth, expireYear, function() {
            $(form).find('input[name=cardno]').remove();
            $(form).find('input[name=cvd]').remove();
            $(form).find('input[name=expireYear]').remove();
            $(form).find('input[name=expireMonth]').remove();
            form.submit();
        });
        return false;
    }
    
    this.createToken = function(cardholder, cardno, cvd, expireMonth, expireYear, callback) {
        var url = serviceRoot + "/api/tokens";
        var that=this;
        jQuery.support.cors = true;
        $.ajax({
            url: url,
            type: "POST",
            beforeSend: function (xhr) { xhr.setRequestHeader ("SimplePayKey", that.publicKey); },
            data: {
                cardNumber:cardno, 
                cvd:cvd,
                expireMonth:expireMonth,
                expireYear:expireYear
            }
        }).done(function(data) {
            callback(data);
        });
    }
    
    this.setPublicKey = function(publicKey) {
        this.publicKey = publicKey;
    }

    this.createUnauthorizedToken = function(orderNumber, description, callback) {
        var url = serviceRoot + "/api/transactions";
        var that=this;
        jQuery.support.cors = true;
        $.ajax({
            url: url,
            type: "POST",
            beforeSend: function (xhr) { xhr.setRequestHeader ("SimplePayKey", that.publicKey); },
            data: {
                orderNumber:orderNumber, 
                description:description
            }
        }).done(function(data) {
            callback(data);
        });
    }
    
    this.authorizeTokenRemote = function(token, amount, currency, returnUrl, cancelUrl) {
        //request url & fields
        var that=this;
        $.ajax({
            url: serviceRoot + "/api/form",
            type: "POST",
            dataType:"json",
            beforeSend: function (xhr) { xhr.setRequestHeader ("SimplePayKey", that.publicKey); },
            data: {
                token:token, 
                amount:amount, 
                currency:currency, 
                returnUrl:returnUrl, 
                cancelUrl:cancelUrl
            }
        }).done(function(data) {
            //inject form
            var inputs = [];
            $.each(data.fields, function(index,val) {
                inputs.push('<input type="hidden" name="'+index+'" value="'+val+'"/>');
            })
            
            $('<form id="_simplepay" method="post" action="'+data.url+'">' + inputs.join('') + '</form>').appendTo('body')[0].submit();
            
        });
        
    }
    
};

var Pay = new PayService();
Pay.init();
