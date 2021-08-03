package helper

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.testcontainers.containers.JdbcDatabaseContainer
import javax.sql.DataSource

fun getDataSource(container: JdbcDatabaseContainer<*>): DataSource {

    val hikariConfig = HikariConfig().apply {
        jdbcUrl = container.jdbcUrl
        username = container.username
        password = container.password
        driverClassName = container.driverClassName
    }

    val dataSource = HikariDataSource(hikariConfig)

    val flyway = Flyway.configure().dataSource(dataSource).load()
    flyway.migrate()

    return dataSource
}
