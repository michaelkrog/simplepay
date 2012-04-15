function PayService (){
    var publicKey;
    var serviceRoot = "79.138.236.100";
    
    this.setPublicKey = function(publicKey) {
        this.publicKey = publicKey;
    }

    this.createUnauthorizedToken = function(orderNumber, description, callback) {
        var url = "http://" + serviceRoot + "/api/transactions";
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
            url: "http://" + serviceRoot + "/api/form",
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

