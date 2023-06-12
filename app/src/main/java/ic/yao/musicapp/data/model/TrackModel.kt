package ic.yao.musicapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TrackModel(
    val id: String,
    val album: String = "",
    val duration: Int,
    val explicit: Boolean,
    val name: String,
    val trackNumber: Int,
    val type: String,
    val popularity: Int,
    val artists: List<String>,
    val isExplicit : Boolean = false
): Parcelable
