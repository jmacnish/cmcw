dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "cmcw"
    password = "w00tch"
    dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
    loggingSql = false // true to log all Hibernate SQL
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost/cmcw_dev"
        }
    }
    test {
        dataSource {
            dbCreate = ""
            url = "jdbc:mysql://localhost/cmcw_dev"
        }
    }
    production {
        dataSource {
            dbCreate = ""
            url = "jdbc:mysql://localhost/cmcw"
        }
    }
}
