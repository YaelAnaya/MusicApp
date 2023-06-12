package ic.yao.musicapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ic.yao.musicapp.util.FAVORITE_ARTISTS_TABLE
import java.sql.Timestamp

@Entity(tableName = FAVORITE_ARTISTS_TABLE)
data class FavoriteArtist(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imageURL: String,
    val followers : Int,
    val genre: String,
    var isFavorite: Boolean = false
)
