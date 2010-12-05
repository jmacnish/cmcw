package cmcw

import oauth.signpost.OAuthConsumer
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.InputSource
import cmcw.VideoType

class ImportController {

    def oauthService
    def catalogService
    def httpClientService
    def netflixService

    def index = {
        def token = [key:'fn3hd8xbjbbte3h4fexk64mp',secret:'Ndz7WtEx7j']
        def uri = 'http://api.netflix.com/catalog/titles/index'

        /*
      def response = oauthService.accessResource(
              uri,
              'netflix',
              token
      )
        */

        final OAuthConsumer consumer = oauthService.getConsumer('netflix')
        def signed = consumer.sign(uri)

        [signed:signed]
    }

    def importCatalog = {
        catalogService.importCatalog '/home/jmacnish/IdeaProjects/netapi/catalog.xml'
    }

    def getDetails = {
        httpClientService.get("http://www.yahoo.com")
    }

    def details = {
        netflixService.details 'http://api.netflix.com/catalog/titles/movies/512381'
    }

}


