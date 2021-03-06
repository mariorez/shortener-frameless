package org.seariver.shortener.application.usecase

import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.port.out.ShortenerRepository
import kotlin.random.Random.Default.nextInt

class ShortenUrlHandler(
    private val repository: ShortenerRepository
) {

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    companion object {
        private const val CODE_LENGTH = 5
    }

    fun handle(command: ShortenUrlCommand) {

        val code = (1..CODE_LENGTH)
            .map { nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        val shortCode = ShortCode(code)
        val shortener = Shortener(command.sourceUrl, shortCode)
        repository.create(shortener)
        command.result = "https://seariver.org/$code"
    }
}
