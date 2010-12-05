package cmcw

import java.util.concurrent.Callable
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils

/**
 * The actual object responsible for making an HTTP call via apache httpclient
 */
public class HttpCall implements Callable<Map> {
  private final HttpClient httpClient;
  private final HttpGet httpGet;
  private final HttpContext context;

  HttpCall(HttpClient httpClient, HttpGet httpGet) {
    this.httpClient = httpClient;
    this.httpGet = httpGet;
    this.context = new BasicHttpContext();
  }

  def Map call() throws Exception {
    try {
      HttpResponse res = httpClient.execute(httpGet, context);
      HttpEntity entity = res.getEntity()
      entity.
      if (entity != null) {
        byte[] bytes = EntityUtils.toByteArray(entity);
        entity.consumeContent()
        return [content: bytes]
      }
    } catch (Exception e) {
      httpGet.abort();
      throw e
    }
    return null
  }
}
