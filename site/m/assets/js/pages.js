$(document).ready(function(){

  curView = 0

  $("#south-link").on("click", function(event){
    if (curView == 0) {
      $(".mobile-container").css({
        'left' : '-100%'
      });
      // $(".navbar").css({
      //   'top' : '0%'
      // });
      curView = 3
    }
  });

  $(".goBackButton").on("click", function(event){
    console.log("Hi")

    if (curView != 0) {
      $(".mobile-container").css({
        'left' : '0%'
      });
      // $(".navbar").css({
      //   'top' : '-10%'
      // });
      curView = 0
    }
  })

});
