package org.seariver.shortener.adapter.`in`

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.seariver.protogen.ShortenRequest
import org.seariver.protogen.ShortenerWriteServiceGrpcKt.ShortenerWriteServiceCoroutineStub
import org.seariver.shortener.GrpcServer

class ShortenerWriteEntrypointTest {

    @Test
    fun `GIVEN valid request MUST return shortener data`() {

        // SETUP
        val address = "localhost"
        val port = 50052

        GrpcServer(port).start()

        val channel = ManagedChannelBuilder
            .forAddress(address, port)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build()

        val stub = ShortenerWriteServiceCoroutineStub(channel)

        // GIVEN
        val url = "https://www.google.com"
        val request = ShortenRequest.newBuilder()
            .setOriginalUrl(url)
            .build()

        runBlocking {
            // WHEN
            val response = stub.createShortenUrl(request)

            // THEN
            response.let {
                assertThat(it.originalUrl).isEqualTo(url)
                assertThat(it.shortenedUrl).isEqualTo("https://seariver.org/qwert12")
            }
        }
    }
}
