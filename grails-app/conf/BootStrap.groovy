import cmcw.CatalogImport
import cmcw.Video
import cmcw.VideoType
import cmcw.Format
import java.text.Normalizer.Form
import cmcw.AvailableFormat

class BootStrap {

    def catalogService

    def nextDay(date, inc) {
        GregorianCalendar c = new GregorianCalendar()
        c.setTime(date)
        c.add(Calendar.DAY_OF_MONTH, inc)
        return c.getTime()
    }

    def generateTestData() {
        def dvdFormat = new Format(type: "dvd", netflixLabel: "DVD")
        def blurayFormat = new Format(type: "bluray", netflixLabel: "Blu-ray")
        def instantFormat = new Format(type: "instant", netflixLabel: "instant")

        def dvdAndBluray = [dvdFormat, blurayFormat] as Set
        def instantOnly = [instantFormat] as Set
        def dvdBlurayInstant = [dvdFormat, blurayFormat,instantFormat] as Set

        // spread!
        [dvdFormat, blurayFormat, instantFormat]*.save()

        def availableFrom = nextDay(new Date(), -4)
        def videos = []
        videos += new Video(title: "Fly Away Home",
                netflixId: "http://api.netflix.com/catalog/titles/movies/512381",
                availableFormats: makeFormatSet(dvdAndBluray, nextDay(availableFrom, 0)),
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/512381.jpg")
        videos += new Video(title: "Forever Young",
                netflixId: "http://api.netflix.com/catalog/titles/movies/517905",
                availableFormats: makeFormatSet(dvdBlurayInstant, nextDay(availableFrom, 1)),
                boxArtLargeUrl: "http://cdn-5.nflximg.com/us/boxshots/large/517905.jpg")
        videos += new Video(title: "Trinity and Beyond: Atomic Bomb",
                netflixId: "http://api.netflix.com/catalog/titles/movies/20767131",
                availableFormats: makeFormatSet(instantOnly, nextDay(availableFrom, 2)),
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/20767131.jpg")
        videos += new Video(title: "Striptease",
                netflixId: "http://api.netflix.com/catalog/titles/movies/1008681",
                availableFormats: makeFormatSet(dvdAndBluray, nextDay(availableFrom, 3)),
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/1008681.jpg")
        videos += new Video(title: "God.com",
                netflixId: "http://api.netflix.com/catalog/titles/movies/20281947",
                availableFormats: makeFormatSet(dvdBlurayInstant, nextDay(availableFrom, 4)),
                boxArtLargeUrl: "http://cdn-7.nflximg.com/us/boxshots/large/20281947.jpg")
        videos += new Video(title: "CNN Millennium 2000",
                netflixId: "http://api.netflix.com/catalog/titles/movies/26211046",
                availableFormats: makeFormatSet(instantOnly, nextDay(availableFrom, 5)),
                boxArtLargeUrl: "http://cdn-6.nflximg.com/us/boxshots/large/26211046.jpg")
        videos += new Video(title: "Fist of Fear, Touch of Death",
                netflixId: "http://api.netflix.com/catalog/titles/movies/507032",
                availableFormats: makeFormatSet(dvdAndBluray, nextDay(availableFrom, 6)),
                boxArtLargeUrl: "http://cdn-2.nflximg.com/us/boxshots/large/507032.jpg")
        videos += new Video(title: "Titanica: IMAX",
                netflixId: "http://api.netflix.com/catalog/titles/movies/13368175",
                availableFormats: makeFormatSet(dvdBlurayInstant, nextDay(availableFrom, 7)),
                boxArtLargeUrl: "http://cdn-5.nflximg.com/us/boxshots/large/13368175.jpg")
        videos += new Video(title: "Steamboat Bill, Jr.",
                netflixId: "http://api.netflix.com/catalog/titles/movies/999095",
                availableFormats: makeFormatSet(instantOnly, nextDay(availableFrom, 8)),
                boxArtLargeUrl: "http://cdn-5.nflximg.com/us/boxshots/large/999095.jpg")
        videos += new Video(title: "The Witches",
                netflixId: "http://api.netflix.com/catalog/titles/movies/20282991",
                availableFormats: makeFormatSet(dvdAndBluray, nextDay(availableFrom, 9)),
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/20282991.jpg")

        videos*.save()

        def videoTypes = [
                new VideoType(netflixIdentifier: 'movies'),
                new VideoType(netflixIdentifier: 'discs'),
                new VideoType(netflixIdentifier: 'programs'),
                new VideoType(netflixIdentifier: 'series')
        ]
        videoTypes*.save()

        def catalogImports = [
                new CatalogImport(file: "/var/lib/tomcat6/webapps/catalog.xml", etag: "0000"),
                new CatalogImport(file: "/var/lib/tomcat6/webapps/catalog456.xml", etag: "0001")
        ]
        catalogImports*.save()
    }

    def init = { servletContext ->
        environments {
            production {
                // No test data.
            }
            development {
                generateTestData()
            }
        }

    }
    def destroy = {
    }

    def makeFormatSet(formats, availableFrom) {
        def set = new HashSet()
        formats.each { format ->
            set += makeFormat(format, availableFrom, AvailableFormat.LastAvailableUntil)
        }
        return set
    }

    def makeFormat(format, availableFrom, availableUntil) {
        def availableFormat = new AvailableFormat(format: format, availableFrom: availableFrom, availableUntil: availableUntil)
        return availableFormat
    }

}
