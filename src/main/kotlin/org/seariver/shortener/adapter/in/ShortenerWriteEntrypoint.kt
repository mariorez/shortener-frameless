package org.seariver.shortener.adapter.`in`

import org.seariver.protogen.ShortenRequest
import org.seariver.protogen.ShortenerResponse
import org.seariver.protogen.ShortenerWriteServiceGrpcKt.ShortenerWriteServiceCoroutineImplBase

class ShortenerWriteEntrypoint : ShortenerWriteServiceCoroutineImplBase() {

    override suspend fun createShortenedUrl(request: ShortenRequest): ShortenerResponse {
        return ShortenerResponse
            .newBuilder()
            .setOriginalUrl(request.originalUrl)
            .setShortenedUrl("https://seariver.org/qwert12")
            .build()
    }
}
