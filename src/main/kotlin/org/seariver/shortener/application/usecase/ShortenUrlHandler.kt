package org.seariver.shortener.application.usecase

import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.port.out.ShortenerRepository

class ShortenUrlHandler(
    private val repository: ShortenerRepository
) {
    fun handle(command: ShortenUrlCommand) {

        val code = "qwert12"
        val shortCode = ShortCode(code)
        val shortener = Shortener(command.originalUrl, shortCode)
        repository.create(shortener)
        command.result = "https://seariver.org/${code}"
    }
}
