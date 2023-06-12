package ic.yao.musicapp.network

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.AlbumResultType
import ic.yao.musicapp.data.model.ArtistModel
import com.adamratzman.spotify.models.SimpleAlbum
import com.adamratzman.spotify.models.SimpleTrack
import com.adamratzman.spotify.utils.Market
import ic.yao.musicapp.core.SpotifyApiHelper
import ic.yao.musicapp.data.model.AlbumModel
import ic.yao.musicapp.data.model.TrackModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotifyService @Inject constructor() {
    private var api: SpotifyAppApi

    init {
        runBlocking { api = SpotifyApiHelper.getApi() }
    }

    suspend fun getAlbums(query: String): List<AlbumModel> {
        return withContext(Dispatchers.IO) {
            val artist = getArtist(query)
            val artistID = artist?.id ?: 0
            val (_, items) = api
                .search.searchAlbum(query = query, market = Market.US)

            items.filter { it.artists.first().id == artistID && it.albumType == AlbumResultType.ALBUM }
                .distinctBy { it.name }
                .sortedWith(compareAlbumsByReleaseDate())
                .map { getAlbumDetails(it) }
        }
    }

    private suspend fun getAlbumDetails(album: SimpleAlbum): AlbumModel {
        return runBlocking {
            val tracks = getAlbumTracks(album.id)
            AlbumModel(
                name = album.name,
                imageURL = album.images.first().url,
                tracks = tracks.map {
                    TrackModel(
                        id = it.id,
                        album = album.name,
                        duration = it.durationMs,
                        explicit = it.explicit,
                        name = it.name,
                        trackNumber = it.trackNumber,
                        type = it.type,
                        popularity = it.popularity ?: 0,
                        isExplicit = it.explicit,
                        artists = it.artists.map { artist -> artist.name }
                    )
                },
                duration = tracks.sumOf { it.durationMs },
                artist = album.artists.first().name,
                releaseYear = album.releaseDate?.year.toString(),
                recordType = album.albumType.toString()
            )
        }
    }

    suspend fun getAlbumTracks(albumID: String): List<SimpleTrack> {
        return runBlocking {
            val (_, items) = api.albums.getAlbumTracks(albumID, market = Market.US)
            items
        }
    }

    suspend fun getArtist(query: String): ArtistModel? {
        return withContext(Dispatchers.Default) {
            val response =
                api.search.searchArtist(query = query, market = Market.US).items.firstOrNull()
            response?.let {
                ArtistModel(
                    id = it.id,
                    name = it.name,
                    imageURL = it.images.firstOrNull()?.url ?: "",
                    genre = it.genres.first(),
                    followers = it.followers.total ?: 0,
                )
            }
        }
    }

    private fun compareAlbumsByReleaseDate(): Comparator<SimpleAlbum> {
        return compareByDescending<SimpleAlbum> { it.releaseDate?.year }
            .thenByDescending { it.releaseDate?.month }
            .thenByDescending { it.releaseDate?.day }
    }
}