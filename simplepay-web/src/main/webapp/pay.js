var Pay = (function(){
    var publicKey;
    
    function setPublicKey(publicKey) {
        this.publicKey = publicKey;
    }

    function createUnauthorizedToken(orderNumber, description, callback) {
        //Create transaction at server and return token
    }
    
    function authorizeToken(token, amount, currency, returnUrl, cancelUrl) {
        //request url & fields
        //inject form
    }
    
    
})();
