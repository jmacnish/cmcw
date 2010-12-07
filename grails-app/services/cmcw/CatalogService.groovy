package cmcw

import groovy.sql.Sql
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.InputSource

/**
 * Catalog related tasks.
 */
class CatalogService {

    static transactional = true
    def netflixService
    def shadowService
    def dataSource
    static def BufSiz = (1024 * 128)

    /**
     * Loads a new catalog (XML from netflix) into the shadow table
     */
    def importCatalog(catalogFile) {
        def content = new File(catalogFile)
        shadowService.drop()
        shadowService.create()
        def batchInsert = shadowService.batchInsert()
        def handler = new RecordsHandler(batchInsert)
        def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
        reader.setContentHandler(handler)
        def inputStream = new BufferedInputStream(new FileInputStream(content), BufSiz)
        reader.parse(new InputSource(inputStream))
        shadowService.index()
    }

    /**
     * Imports new videos from the shadow table.
     */
    def importFromShadow() {
        log.info("Indexing shadow table")
        shadowService.index()
        def sql = new Sql(dataSource)
        /*
         * Add all new movies.
         */
        int count = 0
        def time = System.currentTimeMillis()
        sql.eachRow("select VS.available_from as available_from, VS.available_until as available_until, VS.netflix_id as netflix_id, VS.title as title from video_shadow as VS left join video as V on VS.netflix_id = V.netflix_id where V.netflix_id is null") {
            def video = new Video(title: it.title,
                    netflixId: it.netflix_id,
                    availableFrom: it.available_from,
                    availableUntil: it.available_until,
                    boxArtLargeUrl: null)
            video.save()
            count++
            if (count % 1000 == 0) {
                def delta = (System.currentTimeMillis() - time) / 1000L
                log.info("Imported " + count + " new videos in " + delta + " seconds")
            }
        }
    }

    /**
     * Adds box art to videos, using the details service.
     */
    def augment(videos) {
        videos.each {
            if (it.boxArtLargeUrl == null) {
                def details = netflixService.details(it.netflixId)
                if (details != null) {
                    it.boxArtLargeUrl = details.boxArtLargeUrl
                    log.debug("Found video " + it.id + " with no box art, adding..." + it.boxArtLargeUrl)
                    it.save()
                }
            }
        }
    }

}
