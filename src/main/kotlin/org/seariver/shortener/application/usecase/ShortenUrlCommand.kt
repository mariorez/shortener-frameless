package org.seariver.shortener.application.usecase

import org.seariver.shortener.application.domain.OriginalUrl

data class ShortenUrlCommand(val originalUrl: OriginalUrl) {

    var result: String? = null
}
