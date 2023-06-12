package ic.yao.musicapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ic.yao.musicapp.data.database.dao.ArtistDao
import ic.yao.musicapp.data.database.entities.FavoriteArtist
import ic.yao.musicapp.data.database.entities.RecentSearchedArtist

@Database(
    entities = [
         RecentSearchedArtist::class,
         FavoriteArtist::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MusicAppDatabase : RoomDatabase() {
    abstract fun artistDao() : ArtistDao
}