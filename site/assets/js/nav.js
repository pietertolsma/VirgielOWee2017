
$(document).ready(function(){

  mainMenu = true;
  $("#over-link").on("click", function(event){
    $(".main-container").css({
      'top' : '-50%'
    });
    $(".overlay").css({'opacity' : '1'})
    $("#title").fadeOut(200);
    mainMenu = false;
  });

  $("#sleep-link").click(function(event){
    $(".main-container").css({
      'top' : '50%'
    });
    $(".overlay").css({'opacity' : '1'})
    $("#title").fadeOut(200);
    mainMenu = false;
  });

  $("#programma-link").click(function(event){
    $(".main-container").css({
      'left' : '-50%'
    });
    $(".overlay").css({'opacity' : '1'})
    $("#title").fadeOut(200);
    mainMenu = false;
  });

  $("#vragen-link").click(function(event){
    $(".main-container").css({
      'left' : '50%'
    });
    $(".overlay").css({'opacity' : '1'})
    $("#title").fadeOut(200);
    mainMenu = false;
  });

  $("body").mousemove(function(event){
    if (mainMenu) {
      x = event.pageX;
      y = event.pageY;
      offsetX = x - $(window).width() / 2;
      offsetY = y - $(window).height() / 2;

      if (Math.abs(offsetX) < 90 && Math.abs(offsetY) < 90) {
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
    }
  });


(function ( $ ) {

    $.fn.YouTubePopUp = function(options) {

        var YouTubePopUpOptions = $.extend({
                autoplay: 1
        }, options );

        $(this).on('click', function (e) {
            if (!mainMenu) {
              $(".main-container").css({
                'top' : '0',
                'left' : '0'
              });
              $(".overlay").css({'opacity' : '0.75'})
              $("#title").fadeIn(200);
              mainMenu = true;
              e.preventDefault();
              return false;
            }
            var youtubeLink = $(this).attr("href");

            if( youtubeLink.match(/(youtube.com)/) ){
                var split_c = "v=";
                var split_n = 1;
            }

            if( youtubeLink.match(/(youtu.be)/) || youtubeLink.match(/(vimeo.com\/)+[0-9]/) ){
                var split_c = "/";
                var split_n = 3;
            }

            if( youtubeLink.match(/(vimeo.com\/)+[a-zA-Z]/) ){
                var split_c = "/";
                var split_n = 5;
            }

            var getYouTubeVideoID = youtubeLink.split(split_c)[split_n];

            var cleanVideoID = getYouTubeVideoID.replace(/(&)+(.*)/, "");

            if( youtubeLink.match(/(youtu.be)/) || youtubeLink.match(/(youtube.com)/) ){
                var videoEmbedLink = "https://www.youtube.com/embed/"+cleanVideoID+"?autoplay="+YouTubePopUpOptions.autoplay+"";
            }

            if( youtubeLink.match(/(vimeo.com\/)+[0-9]/) || youtubeLink.match(/(vimeo.com\/)+[a-zA-Z]/) ){
                var videoEmbedLink = "https://player.vimeo.com/video/"+cleanVideoID+"?autoplay="+YouTubePopUpOptions.autoplay+"";
            }

            $("body").append('<div class="YouTubePopUp-Wrap YouTubePopUp-animation"><div class="YouTubePopUp-Content"><span class="YouTubePopUp-Close"></span><iframe src="'+videoEmbedLink+'" allowfullscreen></iframe></div></div>');

            if( $('.YouTubePopUp-Wrap').hasClass('YouTubePopUp-animation') ){
                setTimeout(function() {
                    $('.YouTubePopUp-Wrap').removeClass("YouTubePopUp-animation");
                }, 600);
            }

            $(".YouTubePopUp-Wrap, .YouTubePopUp-Close").click(function(){
                $(".YouTubePopUp-Wrap").addClass("YouTubePopUp-Hide").delay(515).queue(function() { $(this).remove(); });
            });

            e.preventDefault();

        });

        $(document).keyup(function(e) {

            if ( e.keyCode == 27 ){
                $('.YouTubePopUp-Wrap, .YouTubePopUp-Close').click();
            }

        });

    };

}( jQuery ));

jQuery(function(){
  jQuery("a.bla-1").YouTubePopUp();
});
});
