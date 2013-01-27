
function TransactionCrud(key) {
    this._key = key;
    this._listeners = []
    
    this.addListener = function(listener) {
        this._listeners.push(listener);
    }
    
    this._onUpdate = function(transaction) {
        $.each(this._listeners, function(index, val) {
           if(val.onUpdate) {
               val.onUpdate(transaction);
           } 
        });
    }
    
    this.list = function(search, dateBefore, dateAfter, status, callback) {
        var params = {};
        
        search = (search == null ? "" : $.trim(search));
            
        if(search != '') {
            params.searchString = search;
        }
            
        if(status != null && status != '') {
            params.status = $('#status').val();
        }
            
        if(dateBefore != null) {
            params.beforeTimestamp = dateBefore;
        }
        
        if(dateAfter != null) {
            params.afterTimestamp = dateAfter;
        }

        $.ajax({
            url: '../api/transactions',
            data: params/*,
            username:this._key*/
        }).done(callback);
    }
    
    this.read = function(id, callback) {
        $.ajax({
            url: '/api/transactions'+id/*,
            username:this._key*/
        }).done(callback);
    }
    
    this.charge = function(id, amount, callback) {
        var that = this;
        $.ajax({
            url: '../api/transactions/'+id+'/charge' + (amount != null ? '?amount='+amount : ''),
            type:'POST'/*,
            username:this._key*/
        }).done(function(data) {
            that._onUpdate(data);
            callback(data);
        });
    }
    
    this.refund = function(id, amount, callback) {
        var that = this;
        $.ajax({
            url: '../api/transactions/'+id+'/refund' + (amount != null ? '?amount='+amount : ''),
            type:'POST'/*,
            username:this._key*/
        }).done(function(data) {
            that._onUpdate(data);
            callback(data);
        });
    }
    
    this.cancel = function(id, callback) {
        var that = this;
        $.ajax({
            url: '../api/transactions/'+id+'/cancel',
            type:'POST'/*,
            username:this._key*/
        }).done(function(data) {
            that._onUpdate(data);
            callback(data);
        });
    }
}

function Service(key) {
    this.key = key;
    
    this.transactions = new TransactionCrud(key);
    
}


