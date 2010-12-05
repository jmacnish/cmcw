package cmcw

class VideoType {
  String netflixIdentifier
  static constraints = {
    netflixIdentifier(blank: false, unique:true)
  }
}
