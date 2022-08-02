package com.architect.coders.pokedex.data

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MockWebServerRule : TestWatcher() {

    private lateinit var server: MockWebServer

    override fun starting(description: Description) {
        server = MockWebServer()
        server.start(8080)

        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path
                val file = when {
                    path!!.contains("pokemon?limit") -> "pokemon_list.json"
                    path.contains("pokemon/") -> "pokemon_detail.json"
                    else -> ""
                }

                return MockResponse().fromJson(file)
            }
        }
    }

    override fun finished(description: Description) {
        server.shutdown()
    }
}