package org.seariver.shortener.application.port.out

import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.domain.SourceUrl

interface ShortenerRepository {

    fun create(shortener: Shortener)

    fun findBySourceUrl(sourceUrl: SourceUrl): Shortener?
}
