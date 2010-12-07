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
  `available_from` datetime DEFAULT NULL,
  `available_until` datetime DEFAULT NULL,
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
                "insert into video_shadow values (?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        def video = videos.get(i)
                        ps.setDate(1, dateConvert(video.availableFrom))
                        ps.setDate(2, dateConvert(video.availableUntil))
                        ps.setString(3, video.netflixId)
                        ps.setString(4, video.title)
                        ps.setString(5, video.contentHash)
                    }

                    public int getBatchSize() {
                        return videos.size()
                    }
                })
        return updateCounts;
    }

}
