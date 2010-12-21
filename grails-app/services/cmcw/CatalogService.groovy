package cmcw

import groovy.sql.Sql
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.InputSource

/**
 * Catalog related tasks.
 */
class CatalogService {

    static transactional = false
    def netflixService
    def shadowService
    def dataSource
    static def BufSiz = (1024 * 128)

    /**
     * Loads a new catalog (XML from netflix) into the shadow table
     */
    def importCatalog(CatalogImport catalogImport) {
        if (catalogImport.dateImported != null) {
            log.error("CatalogImport:" + catalogImport + " already brought into shadow table")
            return
        }
        def catalogFile = catalogImport.file
        def content = new File(catalogFile)
        shadowService.drop()
        shadowService.create()
        def batchInsert = shadowService.batchInsert()
        def handler = new RecordsHandler(batchInsert, Format.findAll())
        def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
        reader.setContentHandler(handler)
        def inputStream = new BufferedInputStream(new FileInputStream(content), BufSiz)
        reader.parse(new InputSource(inputStream))
        shadowService.index()
        catalogImport.dateImported = new Date()
        catalogImport.save()
    }

    /**
     * Imports new videos from the shadow table.
     */
    def importFromShadow() {
        log.info("Indexing shadow table")
        def sql = new Sql(dataSource)

        /*
         * Add all new movies.
         */

        int count = 0
        def time = System.currentTimeMillis()

        def query = '''select
VS.bluray_available_from as bluray_available_from,
VS.bluray_available_until as bluray_available_until,
VS.dvd_available_from as dvd_available_from,
VS.dvd_available_until as dvd_available_until,
VS.instant_available_from as instant_available_from,
VS.instant_available_until as instant_available_until,
VS.netflix_id as netflix_id,
VS.title as title
from video_shadow as VS
left join video as V on VS.netflix_id = V.netflix_id
where V.netflix_id is null
'''

        def blurayFormat = Format.findByType('bluray')
        def dvdFormat = Format.findByType('dvd')
        def instantFormat = Format.findByType('instant')

        def availableFormatCache = [:]

        sql.eachRow(query) {
            def availableFormats = new HashSet()

            if (it.bluray_available_from != null ^ it.bluray_available_until != null) {
                log.error("bluray: available from !=null!=available_until")
            }
            if (it.dvd_available_from != null ^ it.dvd_available_until != null) {
                log.error("bluray: available from !=null!=available_until")
            }
            if (it.instant_available_from != null ^ it.instant_available_until != null) {
                log.error("bluray: available from !=null!=available_until")
            }

            if (it.bluray_available_from != null || it.bluray_available_until != null) {
                def format = new AvailableFormat(format: blurayFormat,
                        availableFrom: it.bluray_available_from,
                        availableUntil: it.bluray_available_until)
                format = findOrCreateAvailableFormat(format, availableFormatCache)
                availableFormats += format
            }
            if (it.dvd_available_from != null || it.dvd_available_until != null) {
                def format = new AvailableFormat(format: dvdFormat,
                        availableFrom: it.dvd_available_from,
                        availableUntil: it.dvd_available_until)
                format = findOrCreateAvailableFormat(format, availableFormatCache)
                availableFormats += format
            }
            if (it.instant_available_from != null || it.instant_available_until != null) {
                def format = new AvailableFormat(format: instantFormat,
                        availableFrom: it.instant_available_from,
                        availableUntil: it.instant_available_until)
                format = findOrCreateAvailableFormat(format, availableFormatCache)
                availableFormats += format
            }

            def video = new Video(title: it.title,
                    netflixId: it.netflix_id,
                    availableFormats: availableFormats,
                    boxArtLargeUrl: null)
            video.save()
            if (video.hasErrors()) {
                log.error("Could not save video.  Errors=" + video.errors)
            }
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

    /**
     * Either finds an existing AvailableFormat, or creates a new one.
     */
    def findOrCreateAvailableFormat(availableFormat, cache) {
        def cacheAvailableFormat = cache.get(availableFormat)
        if (cacheAvailableFormat == null) {
            cacheAvailableFormat = AvailableFormat.find("from AvailableFormat as AF where AF.format.id = ? and AF.availableFrom = ? and AF.availableUntil = ?",
                    [availableFormat.format.id, availableFormat.availableFrom, availableFormat.availableUntil])
            if (cacheAvailableFormat == null) {
                availableFormat.save()
                cache.put(availableFormat, availableFormat) // looks weird but is ok
                cacheAvailableFormat = availableFormat
            }
        }
        return cacheAvailableFormat
    }

}
