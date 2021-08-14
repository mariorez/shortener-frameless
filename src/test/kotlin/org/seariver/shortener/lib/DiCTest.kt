package org.seariver.shortener.lib

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import java.sql.ResultSet
import java.sql.Statement
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.seariver.protogen.ShortenRequest
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.usecase.ShortenUrlCommand
import org.testcontainers.containers.PostgreSQLContainer

class DiCTest {

    companion object {

        private val dic: DiC = DiC()

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
        val ds = dic.dataSource

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
        val flyway = Flyway.configure().dataSource(dic.dataSource).load()
        flyway.migrate()
        val repository = dic.shortenerRepository

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
    fun `WHEN invoke ShortenUrlHandler THEN return a working ShortenUrlHandler`() {

        // given
        val handler = dic.shortenerHandler
        val repository = dic.shortenerRepository
        val flyway = Flyway.configure().dataSource(dic.dataSource).load()
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

    @Test
    fun `WHEN invoke ShortenerWriteEntrypoint THEN return a working ShortenerWriteEntrypoint`() {

        // given
        val entrypoint = dic.shortenerWriteService
        val repository = dic.shortenerRepository
        val flyway = Flyway.configure().dataSource(dic.dataSource).load()
        flyway.migrate()
        val givenUrl = "https://linux.org"
        val request = ShortenRequest.newBuilder().setSourceUrl(givenUrl).build()

        // when
        runBlocking {
            entrypoint.createShortenedUrl(request)
        }

        // then
        val shortener = repository.findBySourceUrl(SourceUrl(givenUrl))
        assertThat(shortener).isNotNull()
        shortener?.apply {
            assertThat(sourceUrl.url).isEqualTo(givenUrl)
            assertThat(shortCode.code).isNotEmpty()
        }
    }
}
