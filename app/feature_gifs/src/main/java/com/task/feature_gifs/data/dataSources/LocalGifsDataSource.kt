package com.task.feature_gifs.data.dataSources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.task.feature_gifs.domain.model.gifs.GifItem
import com.task.feature_gifs.domain.model.gifs.local.BannedGif
import com.task.feature_gifs.domain.model.gifs.local.Converters
import com.task.feature_gifs.domain.model.gifs.local.GifDao

@Database(
    entities = [GifItem::class, BannedGif::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(value = [Converters::class])
abstract class LocalGifsDataSource : RoomDatabase() {

    abstract fun getGifDao(): GifDao

    companion object {
        fun getInstance(context: Context): LocalGifsDataSource {
            return Room.databaseBuilder(
                context,
                LocalGifsDataSource::class.java,
                "LocalGifsDataSource"
            ).build()
        }
    }
}