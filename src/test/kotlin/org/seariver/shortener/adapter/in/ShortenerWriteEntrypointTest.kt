package org.seariver.shortener.adapter.`in`

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import helper.getChannel
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.seariver.protogen.ShortenRequest
import org.seariver.protogen.ShortenerWriteServiceGrpcKt.ShortenerWriteServiceCoroutineStub
import org.seariver.shortener.GrpcServer
import org.seariver.shortener.lib.DiC
import org.testcontainers.containers.PostgreSQLContainer

@TestInstance(PER_CLASS)
class ShortenerWriteEntrypointTest {

    private var lastGeneratedShortedUrl: String? = null

    companion object {
        private const val PORT = 50052
        private const val ADDRESS = "localhost"
    }

    @BeforeAll
    internal fun setup() {
        val postgres = PostgreSQLContainer<Nothing>("postgres:13")
        postgres.start()
        System.setProperty("jdbc.url", postgres.jdbcUrl)
        System.setProperty("jdbc.username", postgres.username)
        System.setProperty("jdbc.password", postgres.password)
        System.setProperty("jdbc.driverClassName", postgres.driverClassName)

        val dic = DiC()
        val flyway = Flyway.configure().dataSource(dic.getDataSource()).load()
        flyway.migrate()

        GrpcServer(PORT, dic.getShortenerWriteService()).start()
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "https://www.google.com",
            "https://java.io",
            "https://faq.linux.org"]
    )
    fun `GIVEN valid request MUST return shortener data`(url: String) {

        // GIVEN
        val request = ShortenRequest.newBuilder()
            .setSourceUrl(url)
            .build()

        runBlocking {
            // WHEN
            val stub = ShortenerWriteServiceCoroutineStub(getChannel(ADDRESS, PORT))
            val response = stub.createShortenedUrl(request)

            // THEN
            response.apply {
                assertThat(sourceUrl).isEqualTo(url)
                assertThat(shortenedUrl).isNotEqualTo(lastGeneratedShortedUrl)
                lastGeneratedShortedUrl = shortenedUrl
            }
        }
    }
}
