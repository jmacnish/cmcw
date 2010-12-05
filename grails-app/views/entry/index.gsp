<%@ page contentType="text/html;charset=UTF-8" %>
<html>

<head>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
  <link rel="stylesheet" href="${resource(dir:'css',file:'fb.css')}" />
  <script type="text/javascript">
    // we will add our javascript code here
  </script>
  <title>Can't Miss, Can't Watch</title>
</head>

<body>
<div id="fb-root"></div>
<script src="http://connect.facebook.net/en_US/all.js" type="text/javascript"></script>
<script type="text/javascript">
  FB.init({
    appId       : '164406616931287',
    status      : true, // check login status
    cookie      : true, // enable cookies to allow the server to access the session
    xfbml       : true,  // parse XFBML
    channelUrl  : 'http://ideaforge.dlinkddns.com/cmcw/channel.html'  // custom channel
  });
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
  // Binding functions & whatnot
  $(document).ready(function() {
    var enableFB = true;

    // stop normal submit clicks.
    $('button[category=selection]').bind('click', function(event) {
      event.preventDefault(); // prevents the form from being submitted
      $(this).parent().find("button").css("color", "#3b5998");
      $(this).parent().find("button").attr("selected","false");
      $(this).css("color", "#1E2D4D");
      $(this).attr("selected","true");
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
        usePicture = allWatched.parent().find('img').first().attr("src");
        usePictureText = allWatched.parent().find('p').first().text();
        body += "Can't miss: ";
        allWatched.parent().find('p').each(function(index) {
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
        allMissed.parent().find('p').each(function(index) {
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

  <div class="fbgreybox" style="width: 800px;">
    Can't Miss, Can't Watch
  </div>



  <!--
  <div id="logged-in-status" hidden="true">
  </div>
  -->

  <p>
  <h4>Videos for this week:</h4>
</p>

  <form id="submission" action="#" method="get">

    <g:each in="${days}" var="d">

      <!--<div class="container"> -->
      <div id="row_container">
        <div class="fbbluebox" style="width: 800px;">
          <g:formatDate format="EEEE, MMMM dd" date="${d}"/>
        </div>

        <div class="spacer">
          &nbsp;
        </div>

        <g:each in="${videosByDay[d]}" var="v">
          <div class="float">
            <div id="image_container" class="site_img_sz pos-left">
              <a  href="${v.getRealURL()}" title=${v.title} >
                <div id=${v.title}  class="site_img_sz"><img src="${v.boxArtLargeUrl}" width="110" height="150"/></div>
              </a>
            </div id="image_container">
            <button type="submit" class="link" category="selection"><span>watch</span></button>
            <button type="submit" class="link" category="selection"><span>miss</span></button>
        </div>
        </g:each>

        <div class="spacer">
          &nbsp;
        </div>
      </div id="row_container">
    </g:each>

    <button id="post" type="submit" class="link"><span>Post to my wall</span></button>

    <div id="alert">
    </div>

  </form>

</div>
</body>
</html>