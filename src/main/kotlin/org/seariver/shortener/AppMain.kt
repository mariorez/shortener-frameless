package org.seariver.shortener

import org.flywaydb.core.Flyway
import org.seariver.shortener.lib.DiC
import java.io.File
import java.util.*

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
    val dataSource = dic.getDataSource()

    val flyway = Flyway.configure().dataSource(dataSource).load()
    flyway.migrate()

    GrpcServer(50051, dic).run {
        start()
        blockUntilShutdown()
    }
}
