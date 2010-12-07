<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
  <title>API Documentation</title>
  <meta name="layout" content="main" />
  <style type="text/css" media="screen">

  .homePagePanel .panelBody ul {
    list-style-type:none;
    margin-bottom:10px;
  }

  .homePagePanel .panelBody h1 {
    text-transform:uppercase;
    font-size:1.1em;
    margin-bottom:10px;
  }

  .homePagePanel .panelBody {
    padding:15px;
  }

  .homePagePanel .panelBtm {
    height:20px;
  }

  .homePagePanel .panelTop {
    height:11px;
  }
  h2 {
    margin-top:15px;
    margin-bottom:15px;
    font-size:1.2em;
  }
  #pageBody {
    margin-left:280px;
    margin-right:20px;
  }
  </style>
</head>

<body>

<h1>Search catalog</h1>
<h2>cmcw/catalog/search</h2>
<p>Search through the catalog based on criteria</p>

<h3>URL:</h3>
<p class="fixed">http://ideaforge.dlinkddns.com/cmcw/catalog/search</p>

<h3>Formats:</h3>
<p class="fixed">xml, json</p>

<h3>HTTP Method(s):</h3>
<p class="fixed">GET</p>

<h3>Requires Authentication:</h3>
<p class="fixed">false</p>

<h3>Rate Limit:</h3>
<p class="fixed">unlimited</p>

<h3>Parameters:</h3>
<p>The criteria for the catalog search:
<ul>
  <li>availableAfter : A date, in seconds from epoch format, to search for videos with their availability after this date</li>
  <li>videoType : The type of video to restrict searches to:
    <ul>
      <li>movies</li>
      <li>discs</li>
      <li>series</li>
      <li>programs</li>
    </ul>
  </li>
  <li>start : Start index to begin returning results from.  Default is 0.</li>
  <li>count : The maximum number of results to return.  Default is 10.  Count may not exceed 100.</li>
  <li>format : One of 'xml' or 'json'.  Default is 'xml'.</li>
</ul>

<h3>Response:</h3>
<p>Returns a list of videos, with each video containing the following fields:

<ul>
  <li>title : The short title of the Netflix video</li>
  <li>netflixId : The URL ID of the Netflix video</li>
  <li>availableFrom : A date, in seconds from epoch format, that the video is first available from, in any format.  availableFrom may be null or empty for videos that are no longer available for rent.</li>
  <li>availableUntil : A date, in seconds from epoch format, that the video is first available from, in any format.  availableUntil may be null or empty for videos that have no scheduled time to be unavailable (videos typically are perpetually available after they are released)</li>
  <li>boxArtLargeUrl : A URL of the box art for the video, in large format (110px width, 150px height).</li>
</ul>

</p>

<h3>Usage Notes:</h3>
<ul>
  <li>The returned videos are sorted by their availableFrom date.</li>
  <li>Both JSON and XML return formats are not pretty-printed with extra whitespace or newlines, which can
  make them more difficult to read.</li>
  <li>Note that Java dates and times are stored as milliseconds from epoch.  Remember to divide times by 1000L when requesting dates,
  and multiply by 1000L when receiving dates from the API.</li>
</ul>

<h3>Examples:</h3>

<h4>Find all videos that are available after Monday, December 6th, 16:16:19 PST 2010</h4>

<a href="http://ideaforge.dlinkddns.com/cmcw/catalog/search?availableAfter=1291680979">http://ideaforge.dlinkddns.com/cmcw/catalog/search?availableAfter=1291680979</a>

<p>Result:</p>

<pre>
  ${xmlExample.encodeAsHTML()}
</pre>

<h4>Find the first three movies that are available after Monday, December 6th, 16:16:19 PST 2010, as JSON</h4>

<a href="http://ideaforge.dlinkddns.com/cmcw/catalog/search?availableAfter=1291680979&videoType=movies&count=3&format=json">http://ideaforge.dlinkddns.com/cmcw/catalog/search?availableAfter=1291680979&videoType=movies&format=json</a>

<p>Result:</p>

<pre>
  ${jsonExample.encodeAsHTML()}
</pre>

</body>
</html>