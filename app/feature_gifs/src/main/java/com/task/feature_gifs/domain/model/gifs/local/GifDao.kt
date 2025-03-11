package com.task.feature_gifs.domain.model.gifs.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.task.feature_gifs.domain.model.gifs.GifItem

@Dao
interface GifDao {

    // Local
    @Query("""
        SELECT * FROM GifItem 
        WHERE title LIKE '%' || :searchText || '%' 
        OR description LIKE '%' || :searchText || '%'
        OR slug LIKE '%' || :searchText || '%'
        LIMIT :perPage OFFSET (:page - 1) * :perPage
    """)
    suspend fun getLocalGifs(
        page: Int,
        perPage: Int,
        searchText: String
    ): List<GifItem>

    @Upsert
    suspend fun upsertLocalGif(gifItem: GifItem)

    // Banned
    @Query("SELECT * FROM BannedGif WHERE id = :id")
    suspend fun findBannedGifById(id: String): BannedGif?

    @Upsert
    suspend fun insertBannedGif(bannedGif: BannedGif)

    @Query("DELETE FROM GifItem WHERE id = :id")
    suspend fun deleteBannedGifFromLocal(id: String)
}