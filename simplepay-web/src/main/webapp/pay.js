function PayService (){
    this.publicKey;
    this.serviceRoot = "http://simplepay.eu";
    
    this.setPublicKey = function(publicKey) {
        this.publicKey = publicKey;
    }
    
    this.setServiceRoot = function(url) {
        this.serviceRoot = url;
    }

    this.createToken = function(data, callback) {
        var url = this.serviceRoot + "/api/tokens";
        var that=this;
        jQuery.support.cors = true;
        $.ajax({
            url: url,
            type: "POST",
            beforeSend: function (xhr) { xhr.setRequestHeader ("SimplePayKey", that.publicKey); },
            data: {
                orderNumber:data.orderNumber, 
                description:data.description
            }
        }).done(function(data) {
            callback(data);
        });
    }
    
    this.authorizeRemote = function(token, amount, currency, returnUrl, cancelUrl) {
        //request url & fields
        var that=this;
        $.ajax({
            url: this.serviceRoot + "/api/form",
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

