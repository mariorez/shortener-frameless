package org.seariver.shortener.application.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.seariver.shortener.application.domain.OriginalUrl
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.port.out.ShortenerRepository

class ShortenUrlHandlerTest {

    @Test
    fun `GIVEN a valid command MUST persist new shortener data`() {

        // given
        val url = "https://www.google.com.br"
        val command = ShortenUrlCommand(OriginalUrl(url))
        val repository = mock<ShortenerRepository>()

        // when
        val handler = ShortenUrlHandler(repository)
        handler.handle(command)

        // then
        argumentCaptor<Shortener>().apply {
            verify(repository).create(capture())
            assertThat(firstValue.originalUrl.url).isEqualTo(url)
            assertThat(firstValue.shortCode.code).isEqualTo("qwert12")
            assertThat(command.result).isEqualTo("https://seariver.org/qwert12")
        }
    }
}