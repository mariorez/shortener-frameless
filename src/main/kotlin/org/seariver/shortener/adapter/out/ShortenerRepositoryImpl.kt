package org.seariver.shortener.adapter.out

import javax.sql.DataSource
import org.seariver.shortener.application.domain.ShortCode
import org.seariver.shortener.application.domain.Shortener
import org.seariver.shortener.application.domain.SourceUrl
import org.seariver.shortener.application.port.out.ShortenerRepository

class ShortenerRepositoryImpl(
    private val datasource: DataSource
) : ShortenerRepository {

    override fun create(shortener: Shortener) {

        val sql = """
            INSERT INTO shortener (source_url, short_code) 
            values (?, ?)
            """.trimIndent()

        datasource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, shortener.sourceUrl.url)
                stmt.setString(2, shortener.shortCode.code)
                stmt.executeUpdate()
            }
        }
    }

    override fun findBySourceUrl(sourceUrl: SourceUrl): Shortener? {

        var result: Shortener? = null

        val sql = """
            SELECT source_url, short_code 
            FROM shortener
            WHERE source_url = ?
            """.trimIndent()

        datasource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, sourceUrl.url)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        result = Shortener(
                            SourceUrl(rs.getString("source_url")),
                            ShortCode(rs.getString("short_code"))
                        )
                    }
                }
            }
        }

        return result
    }
}
