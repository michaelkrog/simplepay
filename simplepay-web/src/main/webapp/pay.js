var Pay = (function(){
    var publicKey;
    var serviceRoot = "http://localhost:8080";
    
    var setPublicKey = function(publicKey) {
        this.publicKey = publicKey;
    }

    var createUnauthorizedToken = function(orderNumber, description, callback) {
        $.ajax({
          url: serviceRoot + "/transactions",
          username:publicKey,
          type: "POST",
          data: {orderNumber:orderNumber, description:description}
        }).done(function(data) {
            alert(data);
            callback('somedata');
        });
    }
    
    var authorizeTokenRemote = function(token, amount, currency, returnUrl, cancelUrl) {
        //request url & fields
        $.ajax({
          url: serviceRoot + "/authoriation/form",
          username:publicKey,
          type: "POST",
          data: {token:token, amount:amount, currecny:currency, returnUrl:returnUrl, cancelUrl:cancelUrl}
        }).done(function(data) {
            //inject form
            var inputs = [];
            $.each(data.fields, function(index,val) {
                inputs.push('<input type="hidden" name="'+index+'" value="'+val+'"/>');
            })
            
            $('<form id="_simplepay" method="post" action="'+data.url+'">' + inputs.join('') + '</form>').appendTo('body')[0].submit();
            
        });
        
    }
    
})();
