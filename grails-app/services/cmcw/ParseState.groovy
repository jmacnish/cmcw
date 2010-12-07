package cmcw

/**
 * What state the RecordsHandler is in.
 */
enum ParseState {
    NONE, TITLE, AVAILABILITY, FORMAT, ID, EXTERNAL_ID
}

