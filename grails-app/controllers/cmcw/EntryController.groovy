package cmcw

import grails.converters.JSON
import org.springframework.web.client.RestTemplate

/**
 * Handles the Facebook app "Can't Miss Can't Watch" page.
 */
class EntryController {

    def catalogService

    def index = {
        log.debug("params=" + params)

        // Which formats to show
        def displayFormats = Format.findAll()
        def displayFormatIds = []
        displayFormats.each {
            displayFormatIds += it.id
        }

        // Get movies from 4 days ago until 2 days from now.
        def startOfPeriod = getStartOfAdjustedDay(new Date(), -4)
        def endOfPeriod = getStartOfAdjustedDay(new Date(), 2)

        // Show only movies.
        def videoType = 'movies'

        // Start at beginning, show 50.
        def start = 0
        def count = 50

        // Exposing an API is cool but...we don't need to do it.
        def c = Video.createCriteria()
        def videos = c.listDistinct {
            and {
                availableFormats {
                    ge('availableFrom', startOfPeriod)
                    lt('availableFrom', endOfPeriod)
                    format {
                        'in'("id", displayFormatIds)
                    }
                    order('availableFrom', 'desc')
                }
                like("netflixId", '%' + videoType + '%')
            }
            firstResult(start)
            maxResults(count)
        }

        // Add boxshots or anything else we want to have for a "full" video
        catalogService.augment videos

        log.debug("Vidoes=" + videos)

        // Assemble them into daily buckets
        def comp = [
                compare: {a,b-> a.availableFrom.compareTo(b.availableFrom) }
        ] as Comparator

        def videosByDay = [:]
        def days = []
        videos.each { video ->
            def availableFormats = new ArrayList(video.availableFormats)
            Collections.sort(availableFormats, comp)
            def availableFormat = availableFormats.first()
            def day = getStartOfDay(availableFormat.availableFrom)
            if (videosByDay.containsKey(day) == false) {
                days += day
                videosByDay[day] = [] // make a list of videos for each day.
            }
            videosByDay[day] += video
        }

        // May be somewhat sorted order from DB, but could get mixed up with different formats
        Collections.sort(days)

        [params: params, videos: videos, startOfPeriod: startOfPeriod, endOfPeriod: endOfPeriod, days: days, videosByDay: videosByDay]
    }

    /*
     * Some helpers for date manipulation.
     */

    def static getStartOfDay(Date d) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.getTime()
    }

    def static getStartOfWeek(Date d) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            c.add(Calendar.DAY_OF_WEEK, -1)
        }
        return c.getTime()
    }

    def static getWeeksAdjust(date, weeks) {
        GregorianCalendar c = new GregorianCalendar()
        c.setTime(getStartOfWeek(date))
        def adjust = weeks * 7
        c.add(Calendar.DAY_OF_YEAR, adjust)
        return c.getTime()
    }

    def static getStartOfPreviousDay(Date d) {
        return getStartOfAdjustedDay(d, -1)
    }

    /**
     * Skips to start of a previous or next day
     * @param date The current date to adjust
     * @param adjust The number of days to adjust backward (negative) or forward (positive)
     * @return The start of the adjusted day
     */
    def static getStartOfAdjustedDay(Date date, int adjust) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        c.add(Calendar.DAY_OF_WEEK, adjust)
        return c.getTime()
    }

    def static getStartOfMonth(Date d) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        c.set(Calendar.DAY_OF_MONTH, 1)
        return c.getTime()
    }

    def static safeParseDate(field) {
        def ret = null
        if (field != null && field instanceof String) {
            ret = new Date(Long.parseLong(field) * 1000L)
        }
        return ret
    }

    def static dateToEpoch(date) {
        def v = date.getTime() / 1000L
        return v.longValue()
    }

}


/*
* Call the search API to get list of videos back
*/
/*
        def serverURL = grailsApplication.config.grails.serverURL // API URL
        def uri = serverURL + "/catalog/search?availableAfter={availableAfter}&availableUpto={availableUpto}&videoType={videoType}&count=50&format=json"
        def restTemplate = new RestTemplate()
        def vars = [
                'availableAfter': Long.toString(dateToEpoch(startOfPeriod)),
                'availableUpto': Long.toString(dateToEpoch(endOfPeriod)),
                'videoType': 'movies'
        ]
        def result = restTemplate.getForObject(uri, String.class, vars)
        log.debug("Result=" + result)
        def videosMaps = JSON.parse(result)
        def videos = []
        videosMaps.each {
            videos += new Video(title: it.title,
                    netflixId: it.netflixId,
                    availableFrom: safeParseDate(it.availableFrom),
                    availableUntil: safeParseDate(it.availableUntil),
                    boxArtLargeUrl: it.boxArtLargeUrl
            )
        }
*/
