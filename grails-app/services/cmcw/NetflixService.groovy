package cmcw

import oauth.signpost.OAuthConsumer

/**
 * Handles dealing with the netflix API.
 */
class NetflixService {

    static transactional = true
    def oauthService
    def httpClientService

    def details(netflixId) {
        def type = typeFromNetflixId(netflixId)
        def id = idFromNetflixId(netflixId)
        if (type == null || id == null) {
            return null
        }
        final OAuthConsumer consumer = oauthService.getConsumer('netflix')
        def uri = "http://api.netflix.com/catalog/titles/${type.netflixIdentifier}/$id"
        log.debug("type=" + typeFromNetflixId(uri))
        def signed = consumer.sign(uri)
        log.debug("fetching signed url=" + signed)
        def result = httpClientService.get(signed)
        def catalog_title = new XmlSlurper().parseText(result.body)
        def boxArtLargeUrl = catalog_title?.box_art?.@large?.text()
        return [boxArtLargeUrl: boxArtLargeUrl]
    }

    def index() {
        def uri = 'http://api.netflix.com/catalog/titles/index'
        final OAuthConsumer consumer = oauthService.getConsumer('netflix')
        def signedUri = consumer.sign(uri)
        def tmpFile = File.createTempFile("catalog", ".xml")
        def etagFileResponse = httpClientService.get(signedUri, tmpFile)
        log.info("Creating new import catalog")
        def fileName = tmpFile.absolutePath
        def etag = etagFileResponse.etag
        def catalogImport = CatalogImport.findByEtag(etag)
        if (catalogImport == null) {
            log.info("Creating new catalogImport with file=" + fileName + " etag=" + etag)
            catalogImport = new CatalogImport(file: fileName, etag: etag)
            catalogImport.save()
            if (catalogImport.hasErrors()) {
                log.error("Error creating new catalogImport=" + catalogImport.errors)
            }
        } else {
            log.info("Not creating new catalogImport:  import already exists with etag=" + etag)
        }
        return catalogImport
    }

    def typeFromNetflixId(netflixId) {
        log.debug("netflixId=" + netflixId)
        def m = java.util.regex.Pattern.compile(/catalog\/titles\/(.*)\//).matcher(netflixId)
        m.matches()
        if (m[0][1] == null) {
            log.error("Could not get type from netflix id")
        }
        def identifier = m[0][1]
        return VideoType.findByNetflixIdentifier(identifier)
    }

    def idFromNetflixId(netflixId) {
        def m = java.util.regex.Pattern.compile(/catalog\/titles\/[^\/]*\/(.*)/).matcher(netflixId)
        m.matches()
        return m[0][1]
    }

}

