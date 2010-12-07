import cmcw.VideoType
import cmcw.Video
import cmcw.CatalogImport

class BootStrap {

    def catalogService

    def nextDay(date, inc) {
        GregorianCalendar c = new GregorianCalendar()
        c.setTime(date)
        c.add(Calendar.DAY_OF_MONTH, inc)
        return c.getTime()
    }

    def init = { servletContext ->
        def availableFrom = new Date()

        new Video(title:"Fly Away Home",
                netflixId:"http://api.netflix.com/catalog/titles/movies/512381",
                availableFrom: availableFrom,
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/512381.jpg").save()
        new Video(title:"Forever Young",
                netflixId:"http://api.netflix.com/catalog/titles/movies/517905",
                availableFrom: nextDay(availableFrom,1),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-5.nflximg.com/us/boxshots/large/517905.jpg").save()
        new Video(title:"Trinity and Beyond: Atomic Bomb",
                netflixId:"http://api.netflix.com/catalog/titles/movies/20767131",
                availableFrom: nextDay(availableFrom,1),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/20767131.jpg").save()
        new Video(title:"Striptease",
                netflixId:"http://api.netflix.com/catalog/titles/movies/1008681",
                availableFrom: nextDay(availableFrom,2),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/1008681.jpg").save()
        new Video(title:"God.com",
                netflixId:"http://api.netflix.com/catalog/titles/movies/20281947",
                availableFrom: nextDay(availableFrom,2),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-7.nflximg.com/us/boxshots/large/20281947.jpg").save()
        new Video(title:"CNN Millennium 2000",
                netflixId:"http://api.netflix.com/catalog/titles/movies/26211046",
                availableFrom: nextDay(availableFrom,3),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-6.nflximg.com/us/boxshots/large/26211046.jpg").save()
        new Video(title:"Fist of Fear, Touch of Death",
                netflixId:"http://api.netflix.com/catalog/titles/movies/507032",
                availableFrom: nextDay(availableFrom,3),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-2.nflximg.com/us/boxshots/large/507032.jpg").save()
        new Video(title:"Titanica: IMAX",
                netflixId:"http://api.netflix.com/catalog/titles/movies/13368175",
                availableFrom: nextDay(availableFrom,3),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-5.nflximg.com/us/boxshots/large/13368175.jpg").save()
        new Video(title:"Steamboat Bill, Jr.",
                netflixId:"http://api.netflix.com/catalog/titles/movies/999095",
                availableFrom: nextDay(availableFrom,4),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-5.nflximg.com/us/boxshots/large/999095.jpg").save()
        new Video(title:"The Witches",
                netflixId:"http://api.netflix.com/catalog/titles/movies/20282991",
                availableFrom: nextDay(availableFrom,5),
                availableUntil: null,
                boxArtLargeUrl: "http://cdn-1.nflximg.com/us/boxshots/large/20282991.jpg").save()

        new VideoType(netflixIdentifier:'movies').save()
        new VideoType(netflixIdentifier:'discs').save()
        new VideoType(netflixIdentifier:'programs').save()
        new VideoType(netflixIdentifier:'series').save()

        new CatalogImport(file:"/var/lib/tomcat6/webapps/catalog.xml", etag:"0000").save()
    }
    def destroy = {
    }
}
