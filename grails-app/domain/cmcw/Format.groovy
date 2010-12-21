package cmcw

class Format {
    String type
    String netflixLabel
    // should be "foriegnLabel" with a Provider

    static constraints = {
        type(unique: true)
        netflixLabel(unique: true)
    }

    static mapping = {
        cache: 'read-only'
    }

    boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        Format format = (Format) o;

        if (netflixLabel != format.netflixLabel) return false;
        if (type != format.type) return false;

        return true;
    }

    int hashCode() {
        int result;
        result = type.hashCode();
        result = 31 * result + (netflixLabel != null ? netflixLabel.hashCode() : 0);
        return result;
    }
}
