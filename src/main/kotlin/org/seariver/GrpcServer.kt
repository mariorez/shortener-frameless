package org.seariver

import io.grpc.Server
import io.grpc.ServerBuilder
import org.seariver.shortener.adapter.`in`.HelloEntrypoint
import org.slf4j.LoggerFactory

class GrpcServer constructor(
    private val port: Int
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val server: Server = ServerBuilder
        .forPort(port)
        .addService(HelloEntrypoint())
        .build()

    fun start() {
        server.start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("*** shutting down gRPC server since JVM is shutting down")
                this@GrpcServer.stop()
                logger.info("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    val port = 50051
    val server = GrpcServer(port)
    server.start()
    server.blockUntilShutdown()
}