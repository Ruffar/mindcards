
$("form").submit(function(event){

  event.preventDefault();
  var submission = $(this).serialize();
  console.log(submission);

  $.ajax({
    type: "POST",
    url: "/login",
    data: submission,
    error: function(data) {
      console.log(data);
    }
  });

});