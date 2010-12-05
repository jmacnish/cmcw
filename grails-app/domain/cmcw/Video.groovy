package cmcw

class Video {

  String title
  String netflixId
  Date availableFrom
  Date availableUntil
  Date dateCreated
  Date lastUpdated
  String boxArtLargeUrl

  static constraints = {
    title(blank: false)
    netflixId(blank: false, unique:true)
    availableFrom(nullable:true)
    availableUntil(nullable:true)
    boxArtLargeUrl(url:true, nullable:true)
  }

  def getRealURL(){
    // for all the urls
    def firstUrl = (netflixId =~ /api/).replaceFirst("www");

    // for movies
    def realUrl = (firstUrl =~ /catalog\/titles\/movies/).replaceFirst("Movie/show");

    return realUrl;
  }
}
