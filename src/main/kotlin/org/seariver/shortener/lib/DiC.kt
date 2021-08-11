package org.seariver.shortener.lib

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.seariver.shortener.adapter.`in`.ShortenerWriteService
import org.seariver.shortener.adapter.out.ShortenerRepositoryImpl
import org.seariver.shortener.application.port.out.ShortenerRepository
import org.seariver.shortener.application.usecase.ShortenUrlHandler
import javax.sql.DataSource

class DiC {

    fun getDataSource(): DataSource {

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = System.getProperty("jdbc.url")
            username = System.getProperty("jdbc.username")
            password = System.getProperty("jdbc.password")
            driverClassName = System.getProperty("jdbc.driverClassName")
        }

        return HikariDataSource(hikariConfig)
    }

    fun getShortenerRepository(): ShortenerRepository {
        return ShortenerRepositoryImpl(getDataSource())
    }

    fun getShortenUrlHandler(): ShortenUrlHandler {
        return ShortenUrlHandler(getShortenerRepository())
    }

    fun getShortenerWriteService(): ShortenerWriteService {
        return ShortenerWriteService(getShortenUrlHandler())
    }
}
