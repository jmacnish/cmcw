package cmcw

import oauth.signpost.OAuthConsumer


class NetflixService {

    static transactional = true
    def oauthService
    def httpClientService

    def details(netflixId) {
        def type = typeFromNetflixId(netflixId)
        def id = idFromNetflixId(netflixId)
        final OAuthConsumer consumer = oauthService.getConsumer('netflix')
        def uri = "http://api.netflix.com/catalog/titles/${type.netflixIdentifier}/$id"
        log.debug("type=" + typeFromNetflixId(uri))
        def signed = consumer.sign(uri)
        log.debug("fetching signed url=" + signed)
        def result = httpClientService.get(signed)
        def catalog_title = new XmlSlurper().parseText(result.body)
        def boxArtLargeUrl = catalog_title?.box_art?.@large?.text()
        return [boxArtLargeUrl:boxArtLargeUrl]
    }

    def index() {
        def uri = 'http://api.netflix.com/catalog/titles/index'
        final OAuthConsumer consumer = oauthService.getConsumer('netflix')
        def signed = consumer.sign(uri)

        [signed:signed]
    }

    def typeFromNetflixId(netflixId) {
        def m = java.util.regex.Pattern.compile( /catalog\/titles\/(.*)\// ).matcher(netflixId)
        m.matches()
        def identifier = m[0][1]
        return VideoType.findByNetflixIdentifier(identifier)
    }

    def idFromNetflixId(netflixId) {
        def m = java.util.regex.Pattern.compile( /catalog\/titles\/[^\/]*\/(.*)/ ).matcher(netflixId)
        m.matches()
        return m[0][1]
    }


}
