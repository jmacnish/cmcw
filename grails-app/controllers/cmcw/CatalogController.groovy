package cmcw

import grails.converters.XML
import grails.converters.deep.JSON

/**
 * Provides the search API itself.
 */
class CatalogController {

    def catalogService

    def search = {
        def availableAfter = isDate(params, 'availableAfter')
        def availableUpto = isDate(params, 'availableUpto')
        def videoType = isParam(params, 'videoType')
        def c = Video.createCriteria()
        def videos = c.list {
            and {
                if (availableAfter) {
                    availableFormats {
                        ge('availableFrom', availableAfter)
                        order('availableFrom', 'desc')
                    }
                }
                if (availableUpto) {
                    availableFormats {
                        lt('availableFrom', availableUpto)
                    }
                }
                like("netflixId", '%' + videoType + '%')
            }
            firstResult(isInteger(params, 'start', 0))
            maxResults(isInteger(params, 'count', 10))
        }

        // Add boxshots or anything else we want to have for a "full" video
        catalogService.augment videos

        def results = []
        videos.each {
            def formats = []
            it.availableFormats.each { availableFormat ->
                formats += [
                        format: availableFormat.format.type,
                        availableFrom: safeFormDate(availableFormat.availableFrom),
                        availableUntil: safeFormDate(availableFormat.availableUntil)
                        ]
            }
            results +=
                [title: it.title,
                        netflixId: it.netflixId,
                        availableFormats: formats,
                        boxArtLargeUrl: it.boxArtLargeUrl
                ]
        }

        def format = params["format"]
        switch (format) {
            case "json":
                render results as JSON
                break
            case "xml":
                render results as XML
                break
            default:
                render results as XML
        }
    }

    /**
     * Documentation for API
     */
    def documentation = {
        def xmlExample = '''
<?xml version="1.0" encoding="UTF-8"?>
<list>
    <map>
        <entry key="title">Forever Young</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/517905</entry>
        <entry key="availableFrom">1291767224</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-5.nflximg.com/us/boxshots/large/517905.jpg</entry>
    </map>
    <map>
        <entry key="title">Trinity and Beyond: Atomic Bomb</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/20767131</entry>
        <entry key="availableFrom">1291767224</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-1.nflximg.com/us/boxshots/large/20767131.jpg</entry>
    </map>
    <map>
        <entry key="title">Striptease</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/1008681</entry>
        <entry key="availableFrom">1291853624</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-1.nflximg.com/us/boxshots/large/1008681.jpg</entry>
    </map>
    <map>
        <entry key="title">God.com</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/20281947</entry>
        <entry key="availableFrom">1291853624</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-7.nflximg.com/us/boxshots/large/20281947.jpg</entry>
    </map>
    <map>
        <entry key="title">CNN Millennium 2000</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/26211046</entry>
        <entry key="availableFrom">1291940024</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-6.nflximg.com/us/boxshots/large/26211046.jpg</entry>
    </map>
    <map>
        <entry key="title">Fist of Fear, Touch of Death</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/507032</entry>
        <entry key="availableFrom">1291940024</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-2.nflximg.com/us/boxshots/large/507032.jpg</entry>
    </map>
    <map>
        <entry key="title">Titanica: IMAX</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/13368175</entry>
        <entry key="availableFrom">1291940024</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-5.nflximg.com/us/boxshots/large/13368175.jpg</entry>
    </map>
    <map>
        <entry key="title">Steamboat Bill, Jr.</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/999095</entry>
        <entry key="availableFrom">1292026424</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-5.nflximg.com/us/boxshots/large/999095.jpg</entry>
    </map>
    <map>
        <entry key="title">The Witches</entry>
        <entry key="netflixId">http://api.netflix.com/catalog/titles/movies/20282991</entry>
        <entry key="availableFrom">1292112824</entry>
        <entry key="availableUntil"/>
        <entry key="boxArtLargeUrl">http://cdn-1.nflximg.com/us/boxshots/large/20282991.jpg</entry>
    </map>
</list>
'''

        def jsonExample = '''
[
    {
        "title":"Fly Away Home",
        "netflixId":"http://api.netflix.com/catalog/titles/movies/512381",
        "availableFrom":"1291683373",
        "availableUntil":null,
        "boxArtLargeUrl":"http://cdn-1.nflximg.com/us/boxshots/large/512381.jpg"
    },
    {
        "title":"Forever Young",
        "netflixId":"http://api.netflix.com/catalog/titles/movies/517905",
        "availableFrom":"1291769773",
        "availableUntil":null,
        "boxArtLargeUrl":"http://cdn-5.nflximg.com/us/boxshots/large/517905.jpg"
    },
    {
        "title":"Trinity and Beyond: Atomic Bomb",
        "netflixId":"http://api.netflix.com/catalog/titles/movies/20767131",
        "availableFrom":"1291769773",
        "availableUntil":null,
        "boxArtLargeUrl":"http://cdn-1.nflximg.com/us/boxshots/large/20767131.jpg"
    }
]
'''

        [xmlExample: xmlExample, jsonExample: jsonExample]
    }

    /**
     * Helpers
     */
    def isParam(params, theParam) {
        log.debug("params=" + params + " theParam=" + theParam + " here")

        if (params[theParam] && params[theParam] != '') {
            log.debug("param exists with value=" + params[theParam])
            return params[theParam]
        }
        return null
    }

    def isDate(params, theParam) {
        def result = isParam(params, theParam)
        if (result != null) {
            return new Date(Long.parseLong(result) * 1000L)
        }
        return null
    }

    def isInteger(params, theParam, theDefault) {
        def result = isParam(params, theParam)
        if (result != null) {
            return Integer.parseInt(result)
        }
        return theDefault
    }

    def safeFormDate(date) {
        def ret = null
        if (date != null) {
            def value = date.getTime() / 1000L
            ret = Long.toString(value.longValue())
        }
        return ret
    }

}
