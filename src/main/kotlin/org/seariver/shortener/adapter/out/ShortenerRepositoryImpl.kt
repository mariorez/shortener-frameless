package org.seariver.shortener.adapter.out

import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.port.out.ShortenerRepository
import javax.sql.DataSource

class ShortenerRepositoryImpl(
    private val datasource: DataSource
) : ShortenerRepository {

    private val connection = datasource.connection

    override fun create(shortener: Shortener) {
        val sql = """
            INSERT INTO shortener (source_url, short_code) 
            values (?, ?)
            """.trimIndent()

        connection.prepareStatement(sql).run {
            setString(1, shortener.sourceUrl.url)
            setString(2, shortener.shortCode.code)
            executeUpdate()
        }
    }

    override fun findBySourceUrl(sourceUrl: SourceUrl): Shortener? {

        val sql = """
            SELECT source_url, short_code 
            FROM shortener
            WHERE source_url = ?
            """.trimIndent()

        var result: Shortener? = null

        connection.prepareStatement(sql).run {
            setString(1, sourceUrl.url)
            executeQuery().run {
                while (next()) {
                    result = Shortener(
                        SourceUrl(getString("source_url")),
                        ShortCode(getString("short_code"))
                    )
                }
            }
        }

        return result;
    }
}
