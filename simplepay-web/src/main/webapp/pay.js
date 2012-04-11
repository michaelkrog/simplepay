var Pay = (function(){
    var publicKey;
    
    function setPublicKey(publicKey) {
        this.publicKey = publicKey;
    }

    function createUnauthorizedToken(orderNumber, description, amount, currency, returnUrl, cancelUrl, callback) {
        //Create transaction at server and return token
    }
    
    function authorizeToken(token) {
        //request url & fields
        //inject form
    }
    
    
})();
