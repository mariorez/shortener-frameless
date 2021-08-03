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
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource


class ShortenerRepositoryImplTest {

    @Test
    fun `ShortenerRepositoryImpl MUST implement ShortenerRepository interface`() {
        val repository = ShortenerRepositoryImpl(mock(DataSource::class.java))
        assertThat(repository).isInstanceOf(ShortenerRepository::class)
    }

    @Test
    fun `WHEN creating shortener GIVEN valid data MUST persist in database`() {

        // setup
        val container = PostgreSQLContainer<Nothing>("postgres:13")
        container.start()
        val dataSource = getDataSource(container)

        // given
        val originalUrl = OriginalUrl("https://www.google.com")
        val shortCode = ShortCode("qwert")
        val shortener = Shortener(originalUrl, shortCode)
        val repository = ShortenerRepositoryImpl(dataSource)

        // when
        repository.create(shortener)

        // then
        val actual = repository.findByCode(ShortCode("qwert"))
        assertThat(actual).isNotNull()
        assertThat(actual!!.originalUrl.url).isEqualTo(originalUrl.url)
        assertThat(actual!!.shortCode.code).isEqualTo(shortCode.code)
    }
}

