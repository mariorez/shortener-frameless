package org.seariver.shortener.application.usecase

import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.port.out.ShortenerRepository

class ShortenUrlHandler(
    val repository: ShortenerRepository
) {
    fun handle(command: ShortenUrlCommand) {

        val shortCode = ShortCode("qwert")
        val shortener = Shortener(command.originalUrl, shortCode)
        repository.create(shortener)
    }
}
