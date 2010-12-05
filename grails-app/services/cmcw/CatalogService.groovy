package cmcw

import javax.xml.parsers.SAXParserFactory
import org.xml.sax.InputSource

class CatalogService {

  static transactional = true
  def netflixService


  def importCatalog(catalogFile) {
    def content = new File(catalogFile)
    def handler = new RecordsHandler()
    def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
    reader.setContentHandler(handler)
    def inputStream = new FileInputStream(content)
    reader.parse(new InputSource(inputStream))
    log.info("Imported " + Video.count() + " videos.")
  }

  def findByDate(availableAfter) {
    def videos = Video.findAll("from Video as V where V.availableFrom >= ? order by V.availableFrom desc",
            [availableAfter],
            [max:10, offset:0]
            )
    videos.each {
      if (it.boxArtLargeUrl == null) {
        def details = netflixService.details(it.netflixId)
        it.boxArtLargeUrl = details.boxArtLargeUrl
        log.debug("Found video " + it.id + " with no box art, adding..." + it.boxArtLargeUrl)
        it.save()
      }
    }
    return videos
  }
}
