package ic.yao.musicapp.data.model

import android.os.Parcelable
import androidx.compose.foundation.interaction.PressInteraction
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AlbumModel (
    val name: String,
    val artist: String,
    val imageURL: String,
    val duration: Int,
    val releaseYear: String? = null,
    val recordType : String = "",
    val tracks: List<TrackModel>,
) : Parcelable