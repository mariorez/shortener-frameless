package org.seariver.shortener.lib

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.usecase.ShortenUrlCommand
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

class DicTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setup() {
            val postgres = PostgreSQLContainer<Nothing>("postgres:13")
            postgres.start()
            System.setProperty("jdbc.url", postgres.jdbcUrl)
            System.setProperty("jdbc.username", postgres.username)
            System.setProperty("jdbc.password", postgres.password)
            System.setProperty("jdbc.driverClassName", postgres.driverClassName)
        }
    }

    @Test
    fun `WHEN invoke getDataSource THEN return a working DataSource`() {

        // given
        val dic = Dic()
        val ds: DataSource = dic.getDataSource()

        // when
        val statement: Statement = ds.connection.createStatement()
        statement.execute("SELECT 100")
        val resultSet: ResultSet = statement.resultSet
        resultSet.next()

        // then
        assertThat(resultSet.getInt(1)).isEqualTo(100)
    }

    @Test
    fun `WHEN invoke ShortenerRepository THEN return a working ShortenerRepository`() {

        // given
        val dic = Dic()
        val flyway = Flyway.configure().dataSource(dic.getDataSource()).load()
        flyway.migrate()
        val repository = dic.getShortenerRepository()

        // when
        val shortener = repository.findBySourceUrl(SourceUrl("https://google.com"))

        // then
        assertThat(shortener).isNotNull()
        shortener?.apply {
            assertThat(sourceUrl.url).isEqualTo(sourceUrl.url)
            assertThat(shortCode.code).isEqualTo("Qwert")
        }
    }

    @Test
    fun `test name placeholder`() {

        // given
        val dic = Dic()
        val handler = dic.getShortenUrlHandler()
        val repository = dic.getShortenerRepository()
        val flyway = Flyway.configure().dataSource(dic.getDataSource()).load()
        flyway.migrate()
        val givenUrl = "http://seariver.org"

        // when
        handler.handle(ShortenUrlCommand(SourceUrl(givenUrl)))

        // then
        val shortener = repository.findBySourceUrl(SourceUrl(givenUrl))
        assertThat(shortener).isNotNull()
        shortener?.apply {
            assertThat(sourceUrl.url).isEqualTo(givenUrl)
            assertThat(shortCode.code).isNotEmpty()
        }
    }
}
