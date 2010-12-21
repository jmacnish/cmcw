package cmcw

import org.apache.log4j.Logger
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import org.hibernate.annotations.BatchSize

/**
 * Our SAX parser for parsing the Netflix index XML.  We need SAX because we can't parse
 * 340+ MB xml files.
 */
class RecordsHandler extends DefaultHandler {
    static log = Logger.getLogger(RecordsHandler.class)
    static def BatchSize = 10000
    static final def emptyAvailableFrom = AvailableFormat.EarliestAvailableFrom
    static final def emptyAvailableUntil = AvailableFormat.LastAvailableUntil
    def state = ParseState.NONE
    def currentTitle = null
    def currentId = null
    def currentAvailableFrom = null
    def currentAvailableUntil = null
    def count = 0
    def time = System.currentTimeMillis()
    def videoQueue = new ArrayList(BatchSize)
    def batchInsert
    def currentFormatAvailability = [:]
    def formatMap

    RecordsHandler(batchInsert, formats) {
        this.batchInsert = batchInsert
        formatMap = [:]
        formats.each {
            def Format format = it
            formatMap[format.netflixLabel] = format
        }
    }

    void startElement(String ns, String localName, String qName, Attributes atts) {
        switch (qName) {
            case 'title':
                state = ParseState.TITLE
                currentTitle = ''
                break
            case 'availability':
                state = ParseState.AVAILABILITY
                currentAvailableFrom = null
                currentAvailableUntil = null
                def availableFrom = atts.getValue('available_from')
                def availableUntil = atts.getValue('available_until')
                if (availableFrom != null) {
                    def availableFromDate = new Date(Long.parseLong(availableFrom) * 1000L)
                    currentAvailableFrom = availableFromDate
                } else {
                    currentAvailableFrom = emptyAvailableFrom
                }
                if (availableUntil != null) {
                    def availableUntilDate = new Date(Long.parseLong(availableUntil) * 1000L)
                    currentAvailableUntil = availableUntilDate
                } else {
                    currentAvailableUntil = emptyAvailableUntil
                }
                break
            case 'category':
                if (state == ParseState.AVAILABILITY) {
                    def label = atts.getValue('label')
                    currentFormatAvailability[label] =
                        ['currentAvailableFrom':currentAvailableFrom,
                                'currentAvailableUntil':currentAvailableUntil]
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
            case 'availability':
                currentAvailableFrom = null
                currentAvailableUntil = null
                break
            case 'title_index_item': // end of object
                if (currentId != null) {
                    def availableFormats = []
                    currentFormatAvailability.keySet().each {
                        def Format format = formatMap.get(it)
                        def currentFormatAvailable = currentFormatAvailability.get(it)
                        def currentFormatAvailableFrom = currentFormatAvailable.get('currentAvailableFrom')
                        def currentFormatAvailableUntil = currentFormatAvailable.get('currentAvailableUntil')
                        if (format != null) {
                            def availableFormat = new AvailableFormat(format: format,
                                    availableFrom: currentFormatAvailableFrom,
                                    availableUntil: currentFormatAvailableUntil)
                            availableFormats += availableFormat
                        } else {
                            log.error("Could not look up format for format=" + it)
                        }
                    }
                    def video = new Video(title: currentTitle,
                            netflixId: currentId,
                            availableFormats: availableFormats,
                            boxArtLargeUrl: null)
                    video.generateHash()
                    videoQueue += video
                    if (videoQueue.size() == BatchSize) {
                        batchInsert.insert(videoQueue)
                        videoQueue.clear()
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
                currentFormatAvailability.clear()
                state = ParseState.NONE
                break
            case 'external_ids':
                state = ParseState.NONE
                break
            case 'catalog_title_index': // end of stream
                if (videoQueue.size() > 0) {
                    println("Videos remain in queue (" + videoQueue.size() + " videos) -- importing remainder")
                    batchInsert.insert(videoQueue)
                    videoQueue.clear()
                }
                break
        }
    }
}


