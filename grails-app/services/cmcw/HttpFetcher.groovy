package cmcw

import org.apache.commons.logging.LogFactory
import org.apache.http.HttpHost
import org.apache.http.HttpVersion
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.params.ConnManagerParams
import org.apache.http.conn.params.ConnRoutePNames
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.HttpParams
import org.apache.http.params.HttpProtocolParams

/**
 * simple HTTP fetch system, call fetch() to get URL data
 */
class HttpFetcher {
  static log = LogFactory.getLog(HttpFetcher.class)
  def HttpClient httpClient;
  def int maxConnections = 100
  def long timeoutInMillis = 5000 // milliseconds
  def proxyHost
  def proxyPort
  def acceptType = "text/xml"

  def HttpFetcher() {
    HttpParams params = new BasicHttpParams()
    ConnManagerParams.setMaxTotalConnections(params, maxConnections)
    ConnManagerParams.setTimeout(params, timeoutInMillis)
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1)

    // Create and initialize scheme registry
    SchemeRegistry schemeRegistry = new SchemeRegistry()
    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80))
    schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443))

    ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry)
    httpClient = new DefaultHttpClient(cm, params);
  }

  /**
   * Fetch
   */
  def fetch(String uri) {
    try {
      HttpGet newget = new HttpGet(uri);
      newget.addHeader("Accept", acceptType)
      def theCall = new HttpCall(httpClient, newget)
      def result = theCall.call()
      theCall.
      return result
    } catch (IOException ioe) {
      log.error("Got IO exception on fetch while using proxyHost=" + proxyHost + " proxyPort=" + proxyPort)
      throw ioe
    }
  }

}

