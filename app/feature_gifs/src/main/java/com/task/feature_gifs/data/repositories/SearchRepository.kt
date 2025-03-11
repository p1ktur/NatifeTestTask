package com.task.feature_gifs.data.repositories

import com.task.feature_gifs.data.dataSources.RemoteGifsDataSource
import com.task.feature_gifs.domain.model.connection.ConnectionManager
import com.task.feature_gifs.domain.model.gifs.GifItem
import com.task.feature_gifs.domain.model.gifs.local.BannedGif
import com.task.feature_gifs.domain.model.gifs.local.GifDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(
    private val connectionManager: ConnectionManager,
    private val gifDao: GifDao, // LocalGifsDataSource
    private val remoteGifsDataSource: RemoteGifsDataSource
) {

    suspend fun searchGifs(
        page: Int,
        perPage: Int,
        searchText: String
    ): List<GifItem> = withContext(Dispatchers.IO) {
        if (connectionManager.isInternetAvailable()) {
            val gifItems = remoteGifsDataSource
                .searchGifs(page, perPage, searchText)
                .distinctBy { it.id }

            gifItems.forEach { gifItem ->
                if (gifDao.findBannedGifById(gifItem.id) == null) {
                    gifDao.upsertLocalGif(gifItem)
                }
            }

            gifItems.ifEmpty {
                gifDao.getLocalGifs(page, perPage, searchText)
            }
        } else {
            gifDao.getLocalGifs(page, perPage, searchText)
        }
    }

    suspend fun filterBannedGifs(gifItems: List<GifItem>): List<GifItem> {
        return gifItems.filter { gifDao.findBannedGifById(it.id) == null }
    }

    suspend fun banGif(id: String) = withContext(Dispatchers.IO) {
        val bannedGif = BannedGif(id)

        gifDao.insertBannedGif(bannedGif)
        gifDao.deleteBannedGifFromLocal(id)
    }
}