package cmcw

class CatalogController {

  def catalogService

  /**
   * availableAfter: All movies that became availableFrom >= this date
   */
  def search = {
    def availableAfter = new Date(0)
    def availableAfterS = params["availableAfter"]
    println("AvailableAfter=" + availableAfterS)
    if (availableAfterS != null) {
      availableAfter = new Date(Long.parseLong(availableAfterS) * 1000L)
    }
    def videos = catalogService.findByDate(availableAfter)
    [videos:videos]
  }
}
