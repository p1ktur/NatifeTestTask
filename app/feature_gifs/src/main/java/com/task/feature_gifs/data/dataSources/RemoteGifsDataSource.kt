package com.task.feature_gifs.data.dataSources

import com.task.feature_gifs.BuildConfig
import com.task.feature_gifs.domain.http.HttpClient
import com.task.feature_gifs.domain.model.gifs.GifItem
import com.task.feature_gifs.domain.model.gifs.remote.GiphySearchResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.net.URLEncoder

class RemoteGifsDataSource {

    suspend fun searchGifs(
        page: Int,
        perPage: Int,
        searchText: String
    ): List<GifItem> {
        return try {
            val response = HttpClient.get("/search") {
                contentType(ContentType.Application.Json)

                url {
                    parameters.append("api_key", BuildConfig.GIPHY_API_KEY)
                    parameters.append("q", URLEncoder.encode(searchText, "utf-8"))
                    parameters.append("limit", perPage.toString())
                    parameters.append("offset", ((page - 1) * perPage).toString())
                }
            }

            if (response.status != HttpStatusCode.OK) return emptyList()

            val giphyResponse = response.body<GiphySearchResponse>()

            if (giphyResponse.meta.status != HttpStatusCode.OK.value) return emptyList()

            giphyResponse.gifItems
        } catch (_: Exception) {
            emptyList()
        }
    }
}