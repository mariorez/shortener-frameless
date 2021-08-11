package org.seariver.shortener.adapter.`in`

import org.seariver.protogen.ShortenRequest
import org.seariver.protogen.ShortenerResponse
import org.seariver.protogen.ShortenerWriteServiceGrpcKt.ShortenerWriteServiceCoroutineImplBase
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.usecase.ShortenUrlCommand
import org.seariver.shortener.application.usecase.ShortenUrlHandler

class ShortenerWriteService(
    private val handler: ShortenUrlHandler
) : ShortenerWriteServiceCoroutineImplBase() {

    override suspend fun createShortenedUrl(request: ShortenRequest): ShortenerResponse {

        val command = ShortenUrlCommand(SourceUrl(request.sourceUrl))
        handler.handle(command)

        return ShortenerResponse
            .newBuilder()
            .setSourceUrl(command.sourceUrl.url)
            .setShortenedUrl(command.result)
            .build()
    }
}
