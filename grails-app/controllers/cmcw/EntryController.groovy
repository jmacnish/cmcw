package cmcw

import grails.converters.JSON
import org.springframework.web.client.RestTemplate

/**
 * Handles the Facebook app "Can't Miss Can't Watch" page.
 */
class EntryController {

    def catalogService

    def index = {
        // Get movies from 4 days ago until 2 days from now.
        def startOfPeriod = getStartOfAdjustedDay(new Date(), -4)
        def endOfPeriod = getStartOfAdjustedDay(new Date(), 2)

        /*
        * Call the search API to get list of videos back
        */
        def serverURL = grailsApplication.config.grails.serverURL // API URL
        def uri = serverURL + "/catalog/search?availableAfter={availableAfter}&availableUpto={availableUpto}&videoType={videoType}&count=50&format=json"
        def restTemplate = new RestTemplate()
        def vars = [
                'availableAfter': Long.toString(dateToEpoch(startOfPeriod)),
                'availableUpto': Long.toString(dateToEpoch(endOfPeriod)),
                'videoType': 'movies'
        ]
        def result = restTemplate.getForObject(uri, String.class, vars)
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

        /*
         * Assemble them into daily buckets
         */

        def videosByDay = [:]
        def days = []
        videos.each {
            def day = getStartOfDay(it.availableFrom)
            if (videosByDay.containsKey(day) == false) {
                days += day
                videosByDay[day] = []
            }
        }
        videos.each {
            def day = getStartOfDay(it.availableFrom)
            videosByDay[day] += it
        }

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
