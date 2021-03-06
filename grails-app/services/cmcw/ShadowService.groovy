package cmcw

import groovy.sql.Sql
import java.sql.PreparedStatement
import java.sql.SQLException
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate

/**
 * Manages a "shadow" videos table, used for fully importing from the Netflix index.  Faster than just managing our
 * single videos table.
 */
class ShadowService {

    static transactional = true
    def dataSource

    def create() {
        def sql = new Sql(dataSource)
        sql.execute('''create table video_shadow (
  `bluray_available_from` datetime DEFAULT NULL,
  `bluray_available_until` datetime DEFAULT NULL,
  `dvd_available_from` datetime DEFAULT NULL,
  `dvd_available_until` datetime DEFAULT NULL,
  `instant_available_from` datetime DEFAULT NULL,
  `instant_available_until` datetime DEFAULT NULL,
  `netflix_id` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content_hash` varchar(40) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1
''')
    }

    def drop() {
        def sql = new Sql(dataSource)
        sql.execute('''drop table if exists video_shadow''')
    }

    def batchInsert() {
        def batchInsert = new BatchInsert(dataSource)
        return batchInsert
    }

    def index() {
        def sql = new Sql(dataSource)
        // We get duplicate netflixIds from the catalog -- that seems bad.  De-dupe at this import step
        sql.execute('''create table video_shadow_cooked AS SELECT DISTINCT * FROM video_shadow''')
        sql.execute('''drop table video_shadow''')
        sql.execute('''rename table video_shadow_cooked to video_shadow''')
        sql.execute('''create index content_hash_idx on video_shadow (content_hash)''')
        sql.execute('''create unique index netflix_id_idx on video_shadow (netflix_id)''')
    }
}

class BatchInsert {
    def jdbcTemplate

    BatchInsert(dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource)
    }

    def dateConvert(date) {
        if (date != null) {
            return new java.sql.Date(date.getTime())
        } else
            return null
    }

    def insert(videos) {
        int[] updateCounts = jdbcTemplate.batchUpdate(
                "insert into video_shadow values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        def video = videos.get(i)
                        def blurayAvailableFrom = null
                        def blurayAvailableUntil = null
                        def dvdAvailableFrom = null
                        def dvdAvailableUntil = null
                        def instantAvailableFrom = null
                        def instantAvailableUntil = null
                        video.availableFormats.each {
                            def AvailableFormat availableFormat = it
                            def Format format = it.format
                            switch (format.netflixLabel) {
                                case 'DVD':
                                    dvdAvailableFrom = availableFormat.availableFrom
                                    dvdAvailableUntil = availableFormat.availableUntil
                                    break
                                case 'Blu-ray':
                                    blurayAvailableFrom = availableFormat.availableFrom
                                    blurayAvailableUntil = availableFormat.availableUntil
                                    break
                                case 'instant':
                                    instantAvailableFrom = availableFormat.availableFrom
                                    instantAvailableUntil = availableFormat.availableUntil
                                    break
                            }
                        }
                        ps.setDate(1, dateConvert(blurayAvailableFrom))
                        ps.setDate(2, dateConvert(blurayAvailableUntil))
                        ps.setDate(3, dateConvert(dvdAvailableFrom))
                        ps.setDate(4, dateConvert(dvdAvailableUntil))
                        ps.setDate(5, dateConvert(instantAvailableFrom))
                        ps.setDate(6, dateConvert(instantAvailableUntil))
                        ps.setString(7, video.netflixId)
                        ps.setString(8, video.title)
                        ps.setString(9, video.contentHash)
                    }

                    public int getBatchSize() {
                        return videos.size()
                    }
                })
        return updateCounts;
    }

}
