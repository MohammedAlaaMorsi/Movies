package io.mohammedalaamorsi.movies.data.remote

import io.ktor.client.HttpClient

interface HttpClientProvider {
    val httpClientImp: HttpClient
}
