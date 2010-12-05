<%--
  Created by IntelliJ IDEA.
  User: jmacnish
  Date: 11/28/10
  Time: 6:04 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Simple GSP page</title></head>
<body>
<ul>
  <g:each in="${videos}" var="v">
    <li>${v.title}
    <img src="${v.boxArtLargeUrl}">
    </li>
  </g:each>
</ul>
</body>
</html>