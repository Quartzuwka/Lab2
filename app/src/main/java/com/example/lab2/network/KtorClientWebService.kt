package com.example.lab2.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse

class KtorClientWebService : WebService {
    override suspend fun getXMlString(url: String): String {

        val client = HttpClient()

        val response: HttpResponse = client.request(url)
        client.close()
        return response.body()
    }
}
