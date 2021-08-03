package org.seariver.shortener.application.port.out

import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener

interface ShortenerRepository {

    fun create(shortener: Shortener)

    fun findByCode(shortCode: ShortCode): Shortener?
}
