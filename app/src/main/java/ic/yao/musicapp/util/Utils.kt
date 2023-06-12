package ic.yao.musicapp.util

import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import ic.yao.musicapp.data.model.AlbumModel
import ic.yao.musicapp.data.model.ArtistModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.util.concurrent.TimeUnit
import kotlin.math.round

sealed class BottomGraph(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    object HomeScreen :
        BottomGraph(route = "home_screen", title = "Home", icon = Icons.Rounded.Home)

    object FavoriteScreen :
        BottomGraph(route = "favorite_screen", title = "Favorite", icon = Icons.Rounded.Favorite)
}

sealed class Graph(
    val route: String,
    val title: String,
) {
    object RootGraph : Graph(route = "root_graph", title = "Root")
    object BottomNavGraph : Graph(route = "bottom_nav_graph", title = "Bottom Nav")
    object DetailGraph : Graph(route = "detail_graph", title = "Detail")
}

sealed class DetailGraph(
    val route: String,
    val title: String,
) {
    object ArtistDetailScreen : DetailGraph(route = "artist/{id}", title = "Artist Detail") {
        fun passArguments(id: String) = this.route.replace(oldValue = "{id}", newValue = id)
    }

    object AlbumDetailScreen : DetailGraph(route = "album-detail", title = "Album Detail") {
        fun createRoute(albumModel: AlbumModel): String {
            return "album-detail/${Uri.encode(Json.encodeToJsonElement(albumModel).toString())}"
        }
    }
}

val bottomNavScreens = listOf(
    BottomGraph.HomeScreen,
    BottomGraph.FavoriteScreen
)

val graphs = listOf(
    Graph.RootGraph,
    Graph.BottomNavGraph,
    Graph.DetailGraph
)

val detailScreens = listOf(
    DetailGraph.ArtistDetailScreen,
    DetailGraph.AlbumDetailScreen
)

const val FAVORITE_ARTISTS_TABLE = "favorite_artists"
const val RECENT_SEARCHED_ARTISTS_TABLE = "recent_searched_artists"
const val DATABASE_NAME = "music_app_database"

fun getFormattedDuration(duration: Int): String {
    val hours = TimeUnit.MILLISECONDS.toHours(duration.toLong())
    val minutes =
        TimeUnit.MILLISECONDS.toMinutes(duration.toLong()) - TimeUnit.HOURS.toMinutes(hours)
    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(
            hours
        )

    return StringBuilder()
        .append(if (hours > 0) "$hours Hr " else "")
        .append(if (minutes > 0) "$minutes Min" else "")
        .append(if (seconds > 0 && hours < 1) "$seconds Sec" else "")
        .toString()
}

fun getFormattedTrackDuration(duration: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)

    return when {
        minutes > 0 -> {
            StringBuilder()
                .append("$minutes:")
                .append(if (seconds < 10) "0$seconds" else seconds)
                .toString()
        }

        else -> {
            StringBuilder()
                .append("0:")
                .append(if (seconds < 10) "0$seconds:" else seconds)
                .toString()
        }
    }
}

fun getFormattedArtistList(artist: List<ArtistModel>): String {
    return StringBuilder().apply {
        artist.forEachIndexed { index, simpleArtistModel ->
            append(simpleArtistModel.name)
            if (index < artist.size - 1) {
                append(", ")
            }
        }
    }.toString()
}

fun getPopularityStars(
    popularity: Int,
    maxStarts: Int = 5,
):Int {
    val startValue = 100/ maxStarts
    return round(popularity.toDouble() / startValue).toInt()
}

/**
 * Funciones de extensión para el tipo de dato [NavType].
 * */
inline fun <reified T : Parcelable> NavType.Companion.parcelableTypeOf() =
    object : NavType<T>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getParcelable(key) as T?
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putParcelable(key, value)
        }

    }

