package org.seariver.shortener

fun main() {
    val port = 50051
    val server = GrpcServer(port)
    server.start()
    server.blockUntilShutdown()
}
