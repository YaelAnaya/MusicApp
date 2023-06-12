package ic.yao.musicapp.data.repository

import ic.yao.musicapp.data.database.dao.ArtistDao
import ic.yao.musicapp.data.database.entities.FavoriteArtist
import ic.yao.musicapp.data.database.entities.RecentSearchedArtist
import ic.yao.musicapp.network.SpotifyService
import javax.inject.Inject

class MusicAppRepository (
    private val api: SpotifyService,
    private val artistDao: ArtistDao
) {
    suspend fun getAllAlbums(artistId: String) = api.getAlbums(artistId)
    suspend fun getAlbumTracks(albumId: String) = api.getAlbumTracks(albumId)
    suspend fun getArtist(query: String) = api.getArtist(query)

    suspend fun updateRecentSearchedArtist(artis: RecentSearchedArtist) {
        artistDao.updateRecentSearchedArtist(artis)
    }
    suspend fun updateFavoriteArtist(artis: FavoriteArtist) {
        artistDao.updateFavoriteArtist(artis)
    }
    fun getAllRecentSearchedArtists() = artistDao.getAllRecentSearchedArtists()
    fun getAllFavoriteArtists() = artistDao.getAllFavoriteArtists()

    suspend fun addRecentSearchedArtist(artis: RecentSearchedArtist) {
        artistDao.insertRecentSearchedArtist(artis)
    }
    suspend fun addFavoriteArtist(artis: FavoriteArtist) {
        artistDao.insertFavoriteArtist(artis)
    }

    suspend fun removeRecentSearchedArtist(artis: RecentSearchedArtist) {
        artistDao.deleteRecentSearchedArtist(artis)
    }

    suspend fun removeFavoriteArtist(artis: FavoriteArtist) {
        artistDao.deleteFavoriteArtist(artis)
    }

}