package org.seariver.adapter.`in`

import org.seariver.protogen.HelloRequest
import org.seariver.protogen.HelloResponse
import org.seariver.protogen.HelloServiceGrpcKt.HelloServiceCoroutineImplBase

class HelloEntrypoint : HelloServiceCoroutineImplBase() {

    override suspend fun world(request: HelloRequest): HelloResponse {
        return HelloResponse
            .newBuilder()
            .setMessage("Hi ${request.name}! Say Hello World from the FRAMELESS")
            .build()
    }
}
