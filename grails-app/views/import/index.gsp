<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head><title>Catalog Import</title></head>
  <body>

  <g:link controller="import" action="fetchFromNetflix">Fetch catalog from netflix</g:link>   <br/><br/>

  Current catalog imports:
  <table>
    <tr>
      <td>ID</td><td>file</td><td>etag</td><td>Actions</td>
    </tr>
    <g:each in="${catalogImports}" var="i">
    <tr>
      <td>${i.id}</td><td>${i.file}</td><td>${i.etag}</td>
      <td><g:link controller="import" action="loadToShadow" params="[etag:i.etag]">Load to shadow</g:link></td>
    </tr>      
    </g:each> 
  </table>

  <g:link controller="import" action="importFromShadow">Import from Shadow</g:link>   <br/><br/>

  </body>
</html>