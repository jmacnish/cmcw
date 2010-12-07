package cmcw

import org.apache.log4j.Logger
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**
 * Our SAX parser for parsing the Netflix index XML.
 */
class RecordsHandler extends DefaultHandler {
    static log = Logger.getLogger(RecordsHandler.class)
    def state = ParseState.NONE
    def currentTitle = null
    def currentId = null
    def currentAvailableFrom = null
    def currentAvailableUntil = null
    def count = 0
    def time = System.currentTimeMillis()
    def videoQueue = []
    def batchInsert
    static def BatchSize = 10000

    RecordsHandler(batchInsert) {
        this.batchInsert = batchInsert
    }

    void startElement(String ns, String localName, String qName, Attributes atts) {
        switch (qName) {
            case 'title':
                state = ParseState.TITLE
                currentTitle = ''
                break
            case 'availability':
                state = ParseState.AVAILABILITY
                def availableFrom = atts.getValue('available_from')
                def availableUntil = atts.getValue('available_until')
                if (availableFrom != null) {
                    def availableFromDate = new Date(Long.parseLong(availableFrom) * 1000L)
                    if (currentAvailableFrom == null || availableFromDate.compareTo(currentAvailableFrom) < 0) {
                        currentAvailableFrom = availableFromDate
                    }
                }
                if (availableUntil != null) {
                    def availableUntilDate = new Date(Long.parseLong(availableUntil) * 1000L)
                    if (currentAvailableUntil == null || availableUntilDate.compareTo(currentAvailableUntil) > 1) {
                        currentAvailableUntil = availableUntilDate
                    }
                }
                break
            case 'id':
                if (state == ParseState.NONE) {
                    state = ParseState.ID
                    currentId = ''
                }
                break
            case 'external_ids':
                state = ParseState.EXTERNAL_ID
                break
        }
    }

    void characters(char[] chars, int offset, int length) {
        if (state == ParseState.TITLE) {
            currentTitle += new String(chars, offset, length)
        } else if (state == ParseState.ID) {
            currentId += new String(chars, offset, length)
        }
    }

    void endElement(String ns, String localName, String qName) {
        switch (qName) {
            case 'title':
                state = ParseState.NONE
                break
            case 'id':
                if (state == ParseState.ID) {
                    state = ParseState.NONE
                }
                break
            case 'title_index_item': // end of object
                if (currentId != null) {
                    def video = Video.findByNetflixId(currentId)
                    if (video == null) {
                        video = new Video(title: currentTitle, netflixId: currentId, availableFrom: currentAvailableFrom, availableUntil: currentAvailableUntil, boxArtLargeUrl: null)
                    } else {
                        video.title = currentTitle
                        // Do not update or change netflixId
                        video.availableFrom = currentAvailableFrom
                        video.availableUntil = currentAvailableUntil
                    }
                    def persistVideo = false
                    if (persistVideo) {
                        video.save()
                        if (video.hasErrors()) {
                            log.error("Cannot save video=" + video.errors)
                        } else {
                            log.info("Saved video=" + video.title)
                        }
                    }
                    video.generateHash()
                    videoQueue += video
                    if (videoQueue.size() == BatchSize) {
                        batchInsert.insert(videoQueue)
                        videoQueue = []
                    }
                    count++
                    if (count % 1000 == 0) {
                        def delta = (System.currentTimeMillis() - time) / 1000L
                        println("Loaded " + count + " videos in " + delta + " seconds.")
                    }
                }
                // Now reset.
                currentTitle = null
                currentId = null
                currentAvailableFrom = null
                currentAvailableUntil = null
                state = ParseState.NONE
                break
            case 'external_ids':
                state = ParseState.NONE
                break
            case 'catalog_title_index': // end of stream
                if (videoQueue.size() > 0) {
                    println("Videos remain in queue (" + videoQueue.size() + " videos) -- importing remainder")
                    batchInsert.insert(videoQueue)
                    videoQueue = []
                }
                break
        }
    }
}


