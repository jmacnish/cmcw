package cmcw

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.HttpClient
import org.apache.http.client.HttpResponseException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.CoreConnectionPNames
import org.apache.http.params.HttpParams
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger
import org.apache.http.client.methods.HttpHead

/**
 * Fetches stuff over HTTP through a variety of methods.
 */
class HttpClientService {

    static transactional = true
    static def BufSize = 131072

    def get(uri) {
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(uri);
        log.info("executing request " + httpget.getURI());

        ResponseHandler<EtagResponse> responseHandler = new EtagResponseHandler(true)
        EtagResponse response = httpclient.execute(httpget, responseHandler)

        httpclient.getConnectionManager().shutdown()

        return response
    }

    /**
     *
     * @param uri
     * @param file
     * @return an EtagFileResponse object
     */
    def get(uri, file) {
        def HttpParams params = new BasicHttpParams()
        params.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, BufSize)
        HttpClient httpclient = new DefaultHttpClient(params)
        HttpGet httpget = new HttpGet(uri);
        log.info("executing request " + httpget.getURI());

        ResponseHandler<EtagFileResponse> responseHandler = new StreamingEtagResponseHandler(file)
        def response = httpclient.execute(httpget, responseHandler)

        httpclient.getConnectionManager().shutdown()

        return response
    }

    def head(uri) {
        HttpClient httpclient = new DefaultHttpClient();
        def httpHead = new HttpHead(uri); // HEAD not allowed ?
        log.info("executing request " + httpHead.getURI());

        ResponseHandler<EtagResponse> responseHandler = new EtagResponseHandler(false) // dont fetch body.
        EtagResponse response = httpclient.execute(httpHead, responseHandler)

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown()

        return response
    }

}

/**
 * Fetches response and etag into an in-memory object.
 */
class EtagResponseHandler implements ResponseHandler {

    static log = Logger.getLogger(EtagResponseHandler.class)
    def getBody

    EtagResponseHandler(getBody) {
        this.getBody = getBody
    }

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
        def etag = response.getHeaders("ETag")?.first()?.getValue()
        def body = null
        if (getBody == true) {
            HttpEntity entity = response.getEntity()
            if (entity != null) {
                body = EntityUtils.toString(entity)
            }
        }
        return new EtagResponse(etag: etag, body: body)
    }

}

class EtagResponse {
    def etag
    def body
}

/**
 * Fetches the etag for the response, streaming the result to disk.
 */
class StreamingEtagResponseHandler implements ResponseHandler {

    static log = Logger.getLogger(StreamingEtagResponseHandler.class)
    def file
    static def BufSize = 65535

    StreamingEtagResponseHandler(file) {
        this.file = file
    }

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
        if (entity != null) {
            def inStream = entity.getContent()
            def outStream = new BufferedOutputStream(new FileOutputStream(file), BufSize)
            log.debug("writing content to file=" + file)
            try {
                outStream << inStream
            } finally {
                outStream.close()
            }
        }
        def etag = response.getHeaders("ETag")?.first()?.getValue()
        log.debug("etag=" + etag)
        log.debug('file.size=' + file.size() + ' headers=' + response.getAllHeaders())
        return new EtagFileResponse(etag: etag, file: file)
    }

}

class EtagFileResponse {
    def etag
    def file
}
