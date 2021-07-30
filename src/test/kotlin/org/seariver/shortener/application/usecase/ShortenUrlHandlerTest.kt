package org.seariver.shortener.application.usecase

import org.junit.jupiter.api.Test
import org.seariver.shortener.application.domain.OriginalUrl

class ShortenUrlHandlerTest {

    @Test
    fun `test name placeholder`() {

        // given
        val command = ShortenUrlCommand(OriginalUrl("https://www.google.com.br"))

        // when
        val handler = ShortenUrlHandler()
        handler.handle(command)

        // then

    }
}