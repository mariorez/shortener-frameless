package org.seariver.shortener.adapter.out

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import helper.getDataSource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.port.out.ShortenerRepository
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

class ShortenerRepositoryImplTest {

    companion object {
        private val container = PostgreSQLContainer<Nothing>("postgres:13")

        @BeforeAll
        @JvmStatic
        internal fun setup() {
            container.start()
        }
    }

    @Test
    fun `ShortenerRepositoryImpl MUST implement ShortenerRepository interface`() {
        val repository = ShortenerRepositoryImpl(mock(DataSource::class.java))
        assertThat(repository).isInstanceOf(ShortenerRepository::class)
    }

    @Test
    fun `WHEN creating shortener GIVEN valid data MUST persist in database`() {

        // given
        val sourceUrl = SourceUrl("https://www.google.com")
        val shortCode = ShortCode("Qwert")
        val shortener = Shortener(sourceUrl, shortCode)

        // when
        val repository = ShortenerRepositoryImpl(getDataSource(container))
        repository.create(shortener)

        // then
        val actualShortener = repository.findBySourceUrl(sourceUrl)
        assertThat(actualShortener).isNotNull()
        actualShortener?.let {
            assertThat(it.sourceUrl.url).isEqualTo(sourceUrl.url)
            assertThat(it.shortCode.code).isEqualTo(shortCode.code)
        }
    }
}
