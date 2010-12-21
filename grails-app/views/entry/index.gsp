<%@ page contentType="text/html;charset=UTF-8" %>
<html>

<head>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
  <link rel="stylesheet" href="${resource(dir:'css',file:'fb.css')}" />
  <title>Can't Miss, Can't Watch</title>
</head>

<body>
<div id="fb-root"></div>
<script src="http://connect.facebook.net/en_US/all.js" type="text/javascript"></script>
<script type="text/javascript">
  /*
   * Scripting for entry page.
   */

  // If true, enables all Facebook APIs.  If false, suppresses all FB calls.
  var enableFB = true;

  // Some basics
  var colors = new Object();
  colors[true] = "#1E2D4D";
  colors[false] = "#3b5998";

  if (enableFB) {
    // Initialize FB API.

    FB.init({
      appId       : '164406616931287',
      status      : true, // check login status
      cookie      : true, // enable cookies to allow the server to access the session
      xfbml       : true,  // parse XFBML
      channelUrl  : 'http://ideaforge.dlinkddns.com/cmcw/channel.html'  // custom channel
    });
    FB.Canvas.setAutoResize(100);
    FB.getLoginStatus(function(response) {
      if (response.session) {
        $("#logged-in-status").html(
                "You're logged in"
                );
      } else {
        $("#logged-in-status").html(
                "Not logged in"
                );
      }
    });
  }

  /**
   * Turns on a button (either a watch or miss button)
   * @param button A jquery button node that we'll disable
   * @param selected True if button is enabled, false otherwise.
   */
  function setButton(button, selected) {
    $(button).find("span").css("color", colors[selected]);
    if (selected) {
      $(button).attr("selected","true");
    } else {
      $(button).removeAttr("selected");
    }
  }

  /**
   * Returns the selected status of a video
   * @param video A JQuery node to check video status on
   * @return 0: No choice.  -1: miss  1: watch
   */
  function videoState(video) {
    var watchActive = ($(video).find('[action="watch"]').filter('[selected="true"]').length > 0) ? true : false;
    var missActive = ($(video).find('[action="miss"]').filter('[selected="true"]').length > 0) ? true : false;
    if (watchActive == true && missActive == false) {
      return 1;
    } else if (watchActive == false && missActive == true) {
      return -1;
    } else {
      return 0;
    }
  }

  /**
   * Rotate the state through the various options
   * @param state The state to toggle, 0: no choice -1: miss 1: watch
   */
  function toggleState(state) {
    var newState = 0;
    if (state == 0) {
      newState = 1;
    } else if (state == 1) {
      newState = -1;
    }
    return newState;
  }

  /**
   * Half toggles a button (if watch button, 0 -> 1 -> 0, if miss button 0 -> -1 -> 0
   * @param state The current state of a button
   * @param isWatch True if this is a watch button, false if a miss button
   */
  function toggleHalfState(state, isWatch) {
    if (isWatch) {
      if (state == 1) {
        return 0;
      } else {
        return 1;
      }
    } else {
      if (state == -1) {
        return 0;
      } else {
        return -1;
      }
    }
  }

  /**
   * Sets the video absolutely to the given state
   * @param video A jquery video node to set the status for
   * @param state The state to set the video to: -1, 0, 1
   */
  function setVideo(video, state) {
    var watchActive = false;
    var missActive = false;
    if (state == 1) {
      watchActive = true;
    } else if (state == -1) {
      missActive = true;
    }
    setButton($(video).find('[action="watch"]'), watchActive);
    setButton($(video).find('[action="miss"]'), missActive);
    var imgContainer = $(video).parent().find('[category="img"]');
    if (state == 1) {
      $(imgContainer).removeClass("miss");
      $(imgContainer).addClass("watch");
    } else if (state == -1) {
      $(imgContainer).removeClass("watch");
      $(imgContainer).addClass("miss");
    } else {
      $(imgContainer).removeClass("watch");
      $(imgContainer).removeClass("miss");
    }
  }

  /*
   * Given a jquery selection of a category=video node, change the selected status from Nothing -> Watch -> Miss -> Nothing
   * @param video A jquery video node to toggle it's button's selected status.
   */
  function toggle(video) {
    setVideo(video, toggleState(videoState(video)));
  }

  // Bind all the functions on document ready.
  $(document).ready(function() {

    // Stop normal form submit clicks.
    $('button[category=selection]').bind('click', function(event) {
      event.preventDefault(); // prevents the form from being submitted
      var video = $(this).parent();
      var isWatchButton = ($(this).attr("action") == "watch") ? true : false;
      var state = videoState(video);
      setVideo(video, toggleHalfState(state, isWatchButton));
    });

    $('[action=toggle]').bind('click', function(event) {
      var video = $(this).parent().parent().parent().parent().find('[category="video"]');
      toggle(video);
    });

    // submit to wall
    $('#post').bind('click', function(event) {
      event.preventDefault(); // prevents the form from being submitted
      var body = '';
      var usePicture = null;
      var usePictureText = null;
      //var allButtons = $('#submission').find('button[selected="true"], button:contains("watch")').css("color", "black");
      var allSelectedButtons = $('#submission').find('[category=selection]').filter('button[selected="true"]');
      var allWatched = allSelectedButtons.filter('button:contains("watch")');
      var allMissed = allSelectedButtons.filter('button:contains("miss")');
      var maxWatch = 2;
      if (allWatched.length > 0) {
        usePicture = allWatched.parent().prev().find('img').first().attr("src");
        usePictureText = allWatched.parent().find('B').first().text();
        body += "Can't miss: ";
        allWatched.parent().find('B').each(function(index) {
          body += $(this).text();
          if (index < allWatched.length - 1) {
            body += ", ";
          }
        });
      }

      if (allMissed.length > 0) {
        if (allWatched.length > 0) {
          body += "\n\n";
        }
        body += "Can't watch: ";
        allMissed.parent().find('B').each(function(index) {
          body += $(this).text();
          if (index < allMissed.length - 1) {
            body += ", ";
          }
        });
      }

      if (enableFB) {
        FB.login(function(response) {
          if (response.session) {
            if (response.perms) {
              console.log("Logged in OK -- now posting.  Body=" + body);

              var params = {};
              params['message'] = 'New videos to rent this week';
              params['name'] = '';
              params['description'] = body;
              params['link'] = 'http://apps.facebook.com/cantmisscantwatch/';
              params['picture'] = usePicture;
              params['caption'] = null;  // no need for extra text.

              FB.api('/me/feed', 'post', params, function(response) {
                if (!response || response.error) {
                  $('#alert').html('Error occurred while publishing');
                } else {
                  $('#alert').html('Posted message to your wall.');
                }
              });
            } else {
              // user is logged in, but did not grant any permissions
              $('#alert').html('Did not get enough permissions to publish');
            }
          } else {
            $('#alert').html('Need to be logged in');
          }
        }, {perms:'publish_stream'});
      }
    });
  });
</script>

<div class="fbbody">

  <div class="fbgreybox" style="width: 700px;">
    Can't Miss, Can't Watch
  </div>

  <p>
  <h4>
    New movies to watch from <g:formatDate format="EEEE, MMMM d" date="${startOfPeriod}"/> to <g:formatDate format="EEEE, MMMM d" date="${endOfPeriod}"/>
  </h4>
</p>

  <form id="submission" action="#" method="get">

    <g:each in="${days}" var="d">

      <div id="row_container">
        <div class="fbbluebox" style="width: 700px;">
          <g:formatDate format="EEEE, MMMM d" date="${d}"/>
        </div>

        <div class="spacer">
          &nbsp;
        </div>

        <g:each in="${videosByDay[d]}" var="v">
          <div class="float">
            <div id="image_container" class="site_img_sz pos-left">
              <div class="site_img_sz">
                <span category="img"></span>
                <img src="${v.boxArtLargeUrl}" action="toggle" width="110" height="150"/>
              </div>
            </div>
            <p category="video"><B>${v.title}</B><br/>
              <button type="submit" class="link" category="selection" action="watch"><span>watch</span></button>
              <button type="submit" class="link" category="selection" action="miss"><span>miss</span></button>
            </p>
          </div>
        </g:each>

        <div class="spacer">
          &nbsp;
        </div>
      </div>
    </g:each>

    <button id="post" type="submit" class="link"><span>Post to my wall</span></button>

    <div id="alert">
    </div>

  </form>

</div>
</body>
</html>
