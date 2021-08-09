package helper

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.seariver.shortener.GrpcServer

fun getChannel(address: String, port: Int): ManagedChannel {

    GrpcServer(port).start()

    return ManagedChannelBuilder
        .forAddress(address, port)
        .usePlaintext()
        .executor(Dispatchers.Default.asExecutor())
        .build()
}
