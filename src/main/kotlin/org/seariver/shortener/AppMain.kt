package org.seariver.shortener

import java.io.File
import java.util.Properties
import org.flywaydb.core.Flyway
import org.seariver.shortener.lib.DiC

private const val PORT = 50051

fun main() {

    val input = File(
        "/home/mario/Development/shortener-frameless/src/main/resources/application.properties"
    ).inputStream()

    val properties = Properties()
    properties.load(input)

    System.setProperty("jdbc.url", properties.getProperty("jdbc.url"))
    System.setProperty("jdbc.username", properties.getProperty("jdbc.username"))
    System.setProperty("jdbc.password", properties.getProperty("jdbc.password"))
    System.setProperty("jdbc.driverClassName", properties.getProperty("jdbc.driverClassName"))

    val dic = DiC()

    val flyway = Flyway.configure().dataSource(dic.getDataSource()).load()
    flyway.migrate()

    GrpcServer(PORT, dic.getShortenerWriteService()).run {
        start()
        blockUntilShutdown()
    }
}
