package cmcw

import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.Attributes
import javax.swing.text.html.parser.Parser

class RecordsHandler extends DefaultHandler {
  def state = ParseState.NONE
  def currentTitle = null
  def currentId = null
  def currentAvailableFrom = null
  def currentAvailableUntil = null

  void startElement(String ns, String localName, String qName, Attributes atts) {
    println("currentId=" + currentId)
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
        println("Going to save item=" + currentId)
        if (currentId != null) {
          def video = Video.findByNetflixId(currentId)
          if (video == null) {
            video = new Video(title:currentTitle, netflixId:currentId, availableFrom: currentAvailableFrom, availableUntil: currentAvailableUntil, boxArtLargeUrl: null)
          } else {
            video.title = currentTitle
            // Do not update or change netflixId
            video.availableFrom = currentAvailableFrom
            video.availableUntil = currentAvailableUntil
          }
          video.save()
          if (video.hasErrors()) {
            println("Cannot save video=" + video.errors)
          } else {
            println("Saved video=" + video.title)
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
    }
  }
}


