package org.seariver.shortener.lib

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.seariver.shortener.adapter.`in`.ShortenerWriteService
import org.seariver.shortener.adapter.out.ShortenerRepositoryImpl
import org.seariver.shortener.application.port.out.ShortenerRepository
import org.seariver.shortener.application.usecase.ShortenUrlHandler

class DiC {

    private var dataSource: DataSource? = null
    private var shortenerRepository: ShortenerRepository? = null
    private var shortenerHandler: ShortenUrlHandler? = null
    private var shortenerWriteService: ShortenerWriteService? = null

    fun getDataSource(): DataSource {

        return dataSource ?: run {
            val hikariConfig = HikariConfig().apply {
                jdbcUrl = System.getProperty("jdbc.url")
                username = System.getProperty("jdbc.username")
                password = System.getProperty("jdbc.password")
                driverClassName = System.getProperty("jdbc.driverClassName")
            }

            dataSource = HikariDataSource(hikariConfig)
            dataSource as DataSource
        }
    }

    fun getShortenerRepository(): ShortenerRepository {
        return shortenerRepository ?: run {
            shortenerRepository = ShortenerRepositoryImpl(getDataSource())
            shortenerRepository as ShortenerRepository
        }
    }

    fun getShortenUrlHandler(): ShortenUrlHandler {
        return shortenerHandler ?: run {
            shortenerHandler = ShortenUrlHandler(getShortenerRepository())
            shortenerHandler as ShortenUrlHandler
        }
    }

    fun getShortenerWriteService(): ShortenerWriteService {
        return shortenerWriteService ?: run {
            shortenerWriteService = ShortenerWriteService(getShortenUrlHandler())
            shortenerWriteService as ShortenerWriteService
        }
    }
}
