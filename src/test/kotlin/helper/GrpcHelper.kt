package helper

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

fun getChannel(address: String, port: Int): ManagedChannel {

    return ManagedChannelBuilder
        .forAddress(address, port)
        .usePlaintext()
        .executor(Dispatchers.Default.asExecutor())
        .build()
}
