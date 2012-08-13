

function main() {
    $('#email-address-button').click(function() {
        var data = {
            email:$('#email-address').val()
        };
        $.ajax({
          type: 'POST',
          url: "/notificationreceiver",
          data: data,
          dataType: 'json'
        }).done(function(data) {
            alert(data.message);
        });
        
    });
}


$(document).ready(main);