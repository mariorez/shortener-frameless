package org.seariver.shortener

fun main() {

    GrpcServer(50051).run {
        this.start()
        this.blockUntilShutdown()
    }

}
