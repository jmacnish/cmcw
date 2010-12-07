package cmcw

class ImportController {

    def oauthService
    def catalogService
    def httpClientService
    def netflixService

    def index = {
        def catalogImport = netflixService.index()
        [catalogImport: catalogImport]
    }

    def importCatalog = {
        def catalogImport = CatalogImport.findByEtag("0000")
        if (catalogImport != null) {
            log.debug("Importing catalog with file=" + catalogImport.file)
            catalogService.importCatalog catalogImport.file
        }
    }

    def importFromShadow = {
        catalogService.importFromShadow()
    }

    def getDetails = {
        httpClientService.get("http://www.yahoo.com")
    }

    def details = {
        netflixService.details 'http://api.netflix.com/catalog/titles/movies/512381'
    }

}


