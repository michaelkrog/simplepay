






function main() {
    $('#email-address-button').click(function() {
        var data = {
            email:$('#email-address').val()
        };
        $.ajax({
          type: 'POST',
          url: "/notificationreceiver",
          data: data,
          dataType: "text"
        }).done(function(data) {
            alert(data);
        });
        
    });
}


$(document).ready(main);