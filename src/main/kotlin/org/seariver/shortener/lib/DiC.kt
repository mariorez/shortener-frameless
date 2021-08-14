package org.seariver.shortener.lib

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.seariver.shortener.adapter.`in`.ShortenerWriteService
import org.seariver.shortener.adapter.out.ShortenerRepositoryImpl
import org.seariver.shortener.application.port.out.ShortenerRepository
import org.seariver.shortener.application.usecase.ShortenUrlHandler

class DiC {
    val dataSource: DataSource by lazy {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = System.getProperty("jdbc.url")
            username = System.getProperty("jdbc.username")
            password = System.getProperty("jdbc.password")
            driverClassName = System.getProperty("jdbc.driverClassName")
        }
        HikariDataSource(hikariConfig)
    }
    val shortenerRepository: ShortenerRepository by lazy { ShortenerRepositoryImpl(dataSource) }
    val shortenerHandler: ShortenUrlHandler by lazy { ShortenUrlHandler(shortenerRepository) }
    val shortenerWriteService: ShortenerWriteService by lazy { ShortenerWriteService(shortenerHandler) }
}
