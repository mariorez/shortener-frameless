package org.seariver.shortener.application.usecase

import org.seariver.shortener.application.domain.SourceUrl

data class ShortenUrlCommand(val sourceUrl: SourceUrl) {
    var result: String? = null
}
