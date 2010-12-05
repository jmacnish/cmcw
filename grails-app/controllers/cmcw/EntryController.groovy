package cmcw

class EntryController {

  def catalogService


  /*
facebook.applicationSecret='4cb7a6f56c84565df05fb9fee8d15082'
facebook.applicationId='164406616931287'


   */
  def index = {
    def startOfPeriod = getStartOfMonth(new Date())
    log.debug("Start of this week is=" + startOfPeriod)
    def videos = catalogService.findByDate(startOfPeriod)
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

    log.debug("days=" + days + " vbd=" + videosByDay)

    [params:params, videos:videos, startOfPeriod:startOfPeriod, days:days, videosByDay:videosByDay]
  }

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

  def static getStartOfPreviousDay(Date d) {
    GregorianCalendar c = new GregorianCalendar();
    c.setTime(d);
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    c.add(Calendar.DAY_OF_WEEK, -1)
    return c.getTime()
  }

  def static getStartOfMonth(Date d) {
    GregorianCalendar c = new GregorianCalendar();
    c.setTime(d);
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    c.set(Calendar.DAY_OF_MONTH,0)
    return c.getTime()
  }

}
