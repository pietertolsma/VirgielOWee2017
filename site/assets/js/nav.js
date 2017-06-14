$(document).ready(function(){
  $("body").mousemove(function(event){
    x = event.pageX;
    y = event.pageY;
    offsetX = x - $(window).width() / 2;
    offsetY = y - $(window).height() / 2;

    angle = 180 * Math.atan(offsetX / offsetY) / Math.PI;
    if (offsetY > 0) {
      angle += 180;
    }
    $("#compass").css({'transform': 'rotate('+ -angle + 'deg)'});
  });
});
