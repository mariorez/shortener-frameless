package org.seariver.shortener.application.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.seariver.shortener.application.domain.OriginalUrl

class ShortenUrlCommandTest {

    @Test
    fun `GIVEN valid url MUST create a command`() {

        // given
        val url = "https://www.google.com/"
        val originalUrl = OriginalUrl(url)

        // when
        val command = ShortenUrlCommand(originalUrl)

        // then
        assertThat(command.originalUrl.url).isEqualTo(url)
    }
}