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

}


