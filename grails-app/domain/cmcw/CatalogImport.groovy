package cmcw

/**
 * A record of our full fetches from netflix
 */
class CatalogImport {

    String file
    String etag
    Date dateImported
    Date dateCreated
    Date lastUpdated

    static constraints = {
        file blank: false, unique: true
        etag blank: false, unique: true
        dateImported nullable: true
    }
}
