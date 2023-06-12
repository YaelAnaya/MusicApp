package ic.yao.musicapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ic.yao.musicapp.util.RECENT_SEARCHED_ARTISTS_TABLE

@Entity(tableName = RECENT_SEARCHED_ARTISTS_TABLE)
data class RecentSearchedArtist(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imageURL: String,
    val followers : Int,
    val genre: String,
    var isFavorite: Boolean = false
)
