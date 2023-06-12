package ic.yao.musicapp.domain

import ic.yao.musicapp.data.model.AlbumModel
import ic.yao.musicapp.data.repository.MusicAppRepository
import javax.inject.Inject

class GetAlbumsFromArtistUseCase @Inject constructor(
    private  val repository: MusicAppRepository
) {
    suspend operator fun invoke(query: String): List<AlbumModel> = repository.getAllAlbums(query)
}