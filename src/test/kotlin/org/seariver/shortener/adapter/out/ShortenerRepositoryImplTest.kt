package org.seariver.shortener.adapter.out

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import helper.getDataSource
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.seariver.shortener.application.domain.OriginalUrl
import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.port.out.ShortenerRepository
import javax.sql.DataSource

class ShortenerRepositoryImplTest {

    @Test
    fun `ShortenerRepositoryImpl MUST implement ShortenerRepository interface`() {
        val repository = ShortenerRepositoryImpl(mock(DataSource::class.java))
        assertThat(repository).isInstanceOf(ShortenerRepository::class)
    }

    @Test
    fun `WHEN creating shortener GIVEN valid data MUST persist in database`() {

        // given
        val originalUrl = OriginalUrl("https://www.google.com")
        val shortCode = ShortCode("qwert12")
        val shortener = Shortener(originalUrl, shortCode)

        // when
        val repository = ShortenerRepositoryImpl(getDataSource())
        repository.create(shortener)

        // then
        val actual = repository.findByCode(ShortCode("qwert12"))
        assertThat(actual).isNotNull()
        actual?.let {
            assertThat(it.originalUrl.url).isEqualTo(originalUrl.url)
            assertThat(it.shortCode.code).isEqualTo(shortCode.code)
        }
    }
}
