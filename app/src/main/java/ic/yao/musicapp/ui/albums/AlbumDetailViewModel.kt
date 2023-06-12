package ic.yao.musicapp.ui.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ic.yao.musicapp.data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _album = savedStateHandle.get<AlbumModel>("albumId")
    val album: AlbumModel?
        get() = _album
}