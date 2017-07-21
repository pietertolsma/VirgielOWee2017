$(document).ready(function() {
  console.log("Vragen connected.")

  $('h2').on('click', function() {
    if($(this).next().is(":visible")) {
      $(this).prev().css({'transform' : 'rotate('+ 0 +'deg)' , 'transition' : '200ms linear all' });
    } else {
      $(this).prev().css({'transform' : 'rotate('+ 90 +'deg)' , 'transition' : '200ms linear all' });
    }

    $(this).next().slideToggle();
  });
});
