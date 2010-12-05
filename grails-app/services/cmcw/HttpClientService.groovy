package cmcw

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.HttpClient
import org.apache.http.client.HttpResponseException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.apache.commons.logging.LogFactory
import org.apache.http.client.methods.HttpHead

class HttpClientService {

    static transactional = true

    def get(uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(uri);
        log.info("executing request " + httpget.getURI());

        ResponseHandler<EtagResponse> responseHandler = new EtagResponseHandler()
        EtagResponse response = httpclient.execute(httpget, responseHandler)

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown()

        return response
    }

    def head(uri) {
        HttpClient httpclient = new DefaultHttpClient();
        def httpHead = new HttpHead(uri);
        log.info("executing request " + httpHead.getURI());

        ResponseHandler<EtagResponse> responseHandler = new EtagResponseHandler()
        EtagResponse response = httpclient.execute(httpget, responseHandler)

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown()

        return response

    }

}


class EtagResponseHandler implements ResponseHandler {

    static log = LogFactory.getLog(EtagResponseHandler.class)


    /**
     * Returns the response body as a String if the response was successful (a
     * 2xx status code). If no response body exists, this returns null. If the
     * response was unsuccessful (>= 300 status code), throws an
     * {@link HttpResponseException}.
     */
    def handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        StatusLine statusLine = response.getStatusLine()
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase())
        }

        HttpEntity entity = response.getEntity()
        def body = null
        if (entity != null) {
            body = EntityUtils.toString(entity)
        }
        def etag = response.getHeaders("ETag")?.first()
        log.debug('body=' + body + ' headers=' + response.getAllHeaders())

        return new EtagResponse(etag:etag, body:body)
    }

}

class EtagResponse {
    def etag
    def body
}