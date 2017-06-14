$(document).ready(function(){
  $("body").mousemove(function(event){
    x = event.pageX;
    y = event.pageY;
    offsetX = x - $(window).width() / 2;
    offsetY = y - $(window).height() / 2;

    if (Math.abs(offsetX) < 100 && Math.abs(offsetY) < 100) {
      $(".compass").css({
        'background' : '#44d0a6',
        'opacity' : '0.65'
      });
      $("#compass").fadeOut(200);
      $(".play-button").fadeIn(200);
    } else {
      $(".compass").css({
        'background' : 'none',
        'opacity' : '1'
      });
      $(".play-button").fadeOut(0);
      $("#compass").fadeIn(200);
    }

    angle = 180 * Math.atan(offsetX / offsetY) / Math.PI;
    if (offsetY > 0) {
      angle += 180;
    }
    $("#compass").css({'transform': 'rotate('+ -angle + 'deg)'});
  });
});
