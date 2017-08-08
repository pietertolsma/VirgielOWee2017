$(document).ready(function() {
  console.log("Vragen connected.")

  $('.QHeader').on('click', function() {
    console.log('CLICK');
    if($(this).next().is(":visible")) {
      $(this).children().first().css({'transform' : 'rotate('+ 0 +'deg)' , 'transition' : '200ms linear all' });
    } else {
      $(this).children().first().css({'transform' : 'rotate('+ 90 +'deg)' , 'transition' : '200ms linear all' });
    }

    $(this).next().slideToggle();
  });
});
