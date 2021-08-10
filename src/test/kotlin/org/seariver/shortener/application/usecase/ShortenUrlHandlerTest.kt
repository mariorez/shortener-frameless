package org.seariver.shortener.application.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.port.out.ShortenerRepository

@TestInstance(PER_CLASS)
class ShortenUrlHandlerTest {

    private var lastGeneratedCode: String? = null

    @ParameterizedTest
    @ValueSource(strings = [
        "https://www.google.com",
        "https://java.io",
        "https://faq.linux.org"])
    fun `GIVEN a valid command MUST persist new shortener data`(url: String) {

        // given
        val command = ShortenUrlCommand(SourceUrl(url))
        val repository = mock<ShortenerRepository>()

        // when
        val handler = ShortenUrlHandler(repository)
        handler.handle(command)

        // then
        argumentCaptor<Shortener>().apply {
            verify(repository).create(capture())
            assertThat(firstValue.sourceUrl.url).isEqualTo(url)
            assertThat(firstValue.shortCode.code).isNotEqualTo(lastGeneratedCode)
            assertThat(command.result).isEqualTo("https://seariver.org/${firstValue.shortCode.code}")
            lastGeneratedCode = firstValue.shortCode.code
        }
    }
}
