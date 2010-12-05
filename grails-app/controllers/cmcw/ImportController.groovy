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
        def catalogImport = netflixService.index()
        [catalogImport:catalogImport]
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


