package cmcw

import java.security.MessageDigest

class Video {
    String title
    String netflixId
    Date dateCreated
    Date lastUpdated
    String boxArtLargeUrl
    String contentHash
    static hasMany = [availableFormats:AvailableFormat]

    static constraints = {
        title(blank: false)
        netflixId(blank: false, unique: true)
        boxArtLargeUrl(url: true, nullable: true)
        contentHash(maxSize: 40, nullable: true)
    }

    static mapping = {
        contentHash index: 'ContentHash_Idx'
    }

    def getRealURL() {
        // for all the urls
        def firstUrl = (netflixId =~ /api/).replaceFirst("www");

        // for movies
        def realUrl = (firstUrl =~ /catalog\/titles\/movies/).replaceFirst("Movie/show");

        return realUrl;
    }

    def beforeInsert = {
        generateHash()
    }

    def beforeUpdate = {
        generateHash()
    }

    def generateHash() {
        String.metaClass.toSHA1 = { salt = "" ->
            def messageDigest = MessageDigest.getInstance("SHA1")

            messageDigest.update(salt.getBytes())
            messageDigest.update(delegate.getBytes())

            /*
            * Why pad up to 40 characters? Because SHA-1 has an output
            * size of 160 bits. Each hexadecimal character is 4-bits.
            * 160 / 4 = 40
            */
            new BigInteger(1, messageDigest.digest()).toString(16).padLeft(40, '0')
        }
        def content = title
        availableFormats.each {
            content += it.hashCode().toString()
        }
        contentHash = content.toSHA1()
        assert contentHash.length() == 40
        return contentHash
    }

}
