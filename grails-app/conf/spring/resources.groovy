import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import com.mchange.v2.c3p0.ComboPooledDataSource

beans = {

    /**
     * c3P0 pooled data source that forces renewal of DB connections of certain age
     * to prevent stale/closed DB connections and evicts excess idle connections
     * Still using the JDBC configuration settings from DataSource.groovy
     * to have easy environment specific setup available
     */
    dataSource(ComboPooledDataSource) { bean ->
        bean.destroyMethod = 'close'

        //use grails' datasource configuration for connection user, password, driver and JDBC url
        user = CH.config.dataSource.username
        password = CH.config.dataSource.password
        driverClass = CH.config.dataSource.driverClassName
        jdbcUrl = CH.config.dataSource.url

        // Allow up to 50 connections
        maxPoolSize = 50

        //force connections to renew after 4 hours
        maxConnectionAge = 4 * 60 * 60

        //get rid too many of idle connections after 30 minutes
        maxIdleTimeExcessConnections = 30 * 60
    }

}
