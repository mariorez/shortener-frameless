package org.seariver.shortener.adapter.out

import org.seariver.shortener.application.domain.OriginalUrl
import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.port.out.ShortenerRepository
import javax.sql.DataSource

class ShortenerRepositoryImpl(
    private val datasource: DataSource
) : ShortenerRepository {

    private val connection = datasource.connection

    override fun create(shortener: Shortener) {
        val sql = """
            INSERT INTO shortener (original_url, short_code) 
            values (?, ?)
            """.trimIndent()

        connection.prepareStatement(sql).run {
            setString(1, shortener.originalUrl.url)
            setString(2, shortener.shortCode.code)
            executeUpdate()
        }
    }

    override fun findByCode(shortCode: ShortCode): Shortener? {

        val sql = """
            SELECT original_url, short_code 
            FROM shortener
            WHERE short_code = ?
            """.trimIndent()

        var result: Shortener? = null

        connection.prepareStatement(sql).run {
            setString(1, shortCode.code)
            executeQuery().run {
                while (next()) {
                    result = Shortener(
                        OriginalUrl(getString("original_url")),
                        ShortCode(getString("short_code"))
                    )
                }
            }
        }

        return result;
    }
}
