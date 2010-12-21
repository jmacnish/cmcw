package cmcw

class AvailableFormat {
    Format format
    Date availableFrom
    Date availableUntil
    static final def EarliestAvailableFrom = new Date(0)  // start of all rental times
    static final def LastAvailableUntil = new Date(java.lang.Integer.MAX_VALUE * 10000L) // Sat Jul 06 01:21:10 PDT 2650, end of all rental times

    static constraints = {
        availableFrom(min:EarliestAvailableFrom, max:LastAvailableUntil)
        availableUntil(min:EarliestAvailableFrom, max:LastAvailableUntil)
    }

    static mapping = {
        cache: 'read-only'
        availableFrom index:'available_index'
        availableUntil index:'available_index'
    }

    boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        AvailableFormat that = (AvailableFormat) o;

        if (availableFrom != that.availableFrom) return false;
        if (availableUntil != that.availableUntil) return false;
        if (format != that.format) return false;

        return true;
    }

    int hashCode() {
        int result;
        result = format.hashCode();
        result = 31 * result + availableFrom.hashCode();
        result = 31 * result + availableUntil.hashCode();
        return result;
    }

}
