package cmcw

/**
 * Netflix videoType, "movie", "disc", "program" etc.
 */
class VideoType {
    String netflixIdentifier
    static constraints = {
        netflixIdentifier(blank: false, unique: true)
    }
}
