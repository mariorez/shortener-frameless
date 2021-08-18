package org.seariver.shortener

import org.flywaydb.core.Flyway
import org.seariver.shortener.lib.Cdi
import org.seariver.shortener.lib.PropertiesFactory

private const val PORT = 50051

fun main() {

    PropertiesFactory.load()

    Flyway.configure().dataSource(Cdi.dataSource).load().migrate()

    GrpcServer(PORT, Cdi.shortenerWriteService).run {
        start()
        blockUntilShutdown()
    }
}
