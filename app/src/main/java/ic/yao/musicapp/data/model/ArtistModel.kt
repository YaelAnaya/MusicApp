package ic.yao.musicapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ArtistModel (
    val id: String,
    val name: String,
    val imageURL: String,
    val followers : Int,
    val genre: String,
    var isFavorite: Boolean = false
    ) : Parcelable
