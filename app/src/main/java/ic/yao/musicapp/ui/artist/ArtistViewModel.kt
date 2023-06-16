package ic.yao.musicapp.ui.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ic.yao.musicapp.data.database.entities.FavoriteArtist
import ic.yao.musicapp.data.database.entities.RecentSearchedArtist
import ic.yao.musicapp.data.model.ArtistModel
import ic.yao.musicapp.data.repository.MusicAppRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val repository: MusicAppRepository
) : ViewModel() {
    private val _state = MutableLiveData<ArtistState>()
    val state: LiveData<ArtistState> = _state

    init {
        _state.value = ArtistState()
        initState()
    }

    private fun initState() {
        repository.getAllRecentSearchedArtists()
            .asLiveData()
            .observeForever {
                _state.value = _state.value?.copy(
                    artists = it.map { recentSearchedArtist ->
                        ArtistModel(
                            id = recentSearchedArtist.id,
                            name = recentSearchedArtist.name,
                            imageURL = recentSearchedArtist.imageURL,
                            followers = recentSearchedArtist.followers,
                            genre = recentSearchedArtist.genre,
                            isFavorite = recentSearchedArtist.isFavorite,
                        )
                    }
                )
            }
        repository.getAllFavoriteArtists()
            .asLiveData()
            .observeForever {
                _state.value = _state.value?.copy(
                    favoriteArtists = it.map { favoriteArtist ->
                        ArtistModel(
                            id = favoriteArtist.id,
                            name = favoriteArtist.name,
                            imageURL = favoriteArtist.imageURL,
                            followers = favoriteArtist.followers,
                            genre = favoriteArtist.genre,
                            isFavorite = favoriteArtist.isFavorite,
                        )
                    }
                )
            }
    }

    suspend fun searchArtist(artistName: String) = repository.getArtist(artistName)
    suspend fun searchAlbums(artistName: String) = repository.getAllAlbums(artistName)

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Search -> {
                viewModelScope.launch {
                    val artistModel = repository.getArtist(event.searchText)
                    if (artistModel != null) {
                        repository.addRecentSearchedArtist(
                            RecentSearchedArtist(
                                id = artistModel.id,
                                name = artistModel.name,
                                imageURL = artistModel.imageURL,
                                followers = artistModel.followers,
                                genre = artistModel.genre,
                                isFavorite = artistModel.isFavorite,
                            )
                        )
                    }
                    _state.value = _state.value?.copy(
                        searchText = "Search Artist",
                        isSearching = false,
                        artists = _state.value?.artists?.plus(artistModel) ?: listOf()
                    )
                }
            }

            is SearchEvent.SearchActiveChange -> {
                _state.value = _state.value?.copy(
                    isSearching = event.isSearching,
                    searchText = ""
                )
            }

            is SearchEvent.QueryChange -> {
                _state.value = _state.value?.copy(
                    searchText = event.query
                )
            }

            is SearchEvent.ClearSearch -> {
                _state.value = _state.value?.copy(
                    searchText = "Search Artist",
                    isSearching = false,
                )
            }
        }

    }

    fun onFavorite(artist: ArtistModel){
        viewModelScope.launch {
            if (artist.isFavorite) {
                repository.removeFavoriteArtist(
                    FavoriteArtist(
                        id = artist.id,
                        name = artist.name,
                        imageURL = artist.imageURL,
                        followers = artist.followers,
                        genre = artist.genre,
                        isFavorite = false,
                    )
                )
                repository.updateRecentSearchedArtist(
                    RecentSearchedArtist(
                        id = artist.id,
                        name = artist.name,
                        imageURL = artist.imageURL,
                        followers = artist.followers,
                        genre = artist.genre,
                        isFavorite = false,
                    ))
            } else {
                repository.addFavoriteArtist(
                    FavoriteArtist(
                        id = artist.id,
                        name = artist.name,
                        imageURL = artist.imageURL,
                        followers = artist.followers,
                        genre = artist.genre,
                        isFavorite = true,
                    )
                )
                repository.updateRecentSearchedArtist(
                    RecentSearchedArtist(
                        id = artist.id,
                        name = artist.name,
                        imageURL = artist.imageURL,
                        followers = artist.followers,
                        genre = artist.genre,
                        isFavorite = true,
                    ))
            }
            initState()
        }

    }

    sealed interface SearchEvent {
        data class Search(val searchText: String) : SearchEvent
        data class SearchActiveChange(val isSearching: Boolean) : SearchEvent
        data class QueryChange(val query: String) : SearchEvent
        object ClearSearch : SearchEvent
    }

    data class ArtistState(
        var artists: List<ArtistModel?> = mutableListOf(),
        var favoriteArtists: List<ArtistModel?> = mutableListOf(),
        var searchText: String = "Search Artist",
        var isSearching: Boolean = false,
    )
}