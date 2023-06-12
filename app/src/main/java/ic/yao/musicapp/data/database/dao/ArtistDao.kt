package ic.yao.musicapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ic.yao.musicapp.data.database.entities.FavoriteArtist
import ic.yao.musicapp.data.database.entities.RecentSearchedArtist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearchedArtist(recentSearchedArtist: RecentSearchedArtist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArtist(favoriteArtist: FavoriteArtist)

    @Update
    suspend fun updateRecentSearchedArtist(recentSearchedArtist: RecentSearchedArtist)

    @Update
    suspend fun updateFavoriteArtist(favoriteArtist: FavoriteArtist)

    @Delete
    suspend fun deleteFavoriteArtist(favoriteArtist: FavoriteArtist)

    @Delete
    suspend fun deleteRecentSearchedArtist(recentSearchedArtist: RecentSearchedArtist)

    @Query("SELECT * FROM recent_searched_artists ORDER BY name ASC")
    fun getAllRecentSearchedArtists(): Flow<List<RecentSearchedArtist>>

    @Query("SELECT * FROM favorite_artists ORDER BY name ASC")
    fun getAllFavoriteArtists(): Flow<List<FavoriteArtist>>
}