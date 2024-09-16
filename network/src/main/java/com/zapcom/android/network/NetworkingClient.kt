package com.zapcom.android.networking

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier

/**
 * Networking client
 *
 * @property networkConfig
 * @constructor Create empty Networking client
 */
@Singleton
class NetworkingClient @Inject constructor(private val networkConfig: NetworkConfig) {

    @PublishedApi
    internal val client = HttpClient(Android) {
        engine {
            // Disable hostname verification
            this.sslManager = { httpsURLConnection ->
                httpsURLConnection.hostnameVerifier = HostnameVerifier { _, _ -> true }
            }
        }
        defaultRequest {
            url{
                protocol = URLProtocol.HTTPS
                host = networkConfig.baseURL
            }

        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    /**
     * Get
     *
     * @param T
     * @param path
     * @param block
     * @receiver
     * @return
     */
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Throws(HttpException::class)
    suspend inline fun <reified T> get(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T =
        try {
            request(path, block)
        } catch (e: Throwable) {
            throw HttpException("shiva:"+e.toString(), e)
        }

    /**
     * Request
     *
     * @param T
     * @param path
     * @param block
     * @receiver
     * @return
     */

    @PublishedApi
    internal suspend inline fun <reified T> request(
        path: String,
        block: HttpRequestBuilder.() -> Unit,
    ): T =
        client.request(
            HttpRequestBuilder()
                .apply {
                    contentType()
                }.apply {
                    url { path(path) }
                }
                .apply(block),
        ).body()

}