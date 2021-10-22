
$("form").submit(function(event){

  event.preventDefault();
  var submission = $(this).serializeArray();

  $.ajax({
    type: "POST",
    url: "/login",
    data: {
      email: submission[0].value,
      password: submission[1].value
    },
    success: function(response) {
      var urlParams = new URLSearchParams(window.location.search);
      var returnUrl = urlParams.get("return");
      console.log(returnUrl);
    },
    error: function(response) {
      var errorMsg = response.responseJSON.message;
      $(errorLabel).text(errorMsg);
    }
  });

});