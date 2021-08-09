package org.seariver.shortener.adapter.`in`

import assertk.assertThat
import assertk.assertions.isEqualTo
import helper.getChannel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.seariver.protogen.ShortenRequest
import org.seariver.protogen.ShortenerWriteServiceGrpcKt.ShortenerWriteServiceCoroutineStub

class ShortenerWriteEntrypointTest {

    @Test
    fun `GIVEN valid request MUST return shortener data`() {

        // GIVEN
        val url = "https://www.google.com"
        val request = ShortenRequest.newBuilder()
            .setSourceUrl(url)
            .build()

        runBlocking {
            // WHEN
            val stub = ShortenerWriteServiceCoroutineStub(getChannel("localhost", 50052))
            val response = stub.createShortenedUrl(request)

            // THEN
            response.let {
                assertThat(it.sourceUrl).isEqualTo(url)
                assertThat(it.shortenedUrl).isEqualTo("https://seariver.org/Qwert")
            }
        }
    }
}
