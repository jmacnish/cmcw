package cmcw

/**
 * Some administrative tasks -- index the catalog, load a catalog, etc
 * TODO: These should all be secured.
 */
class ImportController {

    def oauthService
    def catalogService
    def httpClientService
    def netflixService

    def index = {
        def catalogImports = CatalogImport.findAll()
        log.debug("ALl catalog imports=" + catalogImports)
        [catalogImports: catalogImports]
    }

    def fetchFromNetflix = {
        def catalogImport = netflixService.index()
        [catalogImport: catalogImport]
    }

    def loadToShadow = {
        def etag = params["etag"]
        def catalogImport = CatalogImport.findByEtag(etag)
        if (catalogImport != null) {
            log.debug("Importing catalog with file=" + catalogImport.file)
            catalogService.importCatalog catalogImport
        }
    }

    def importFromShadow = {
        catalogService.importFromShadow()
    }

}


