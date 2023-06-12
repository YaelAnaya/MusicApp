package ic.yao.musicapp.domain

import ic.yao.musicapp.data.model.ArtistModel
import ic.yao.musicapp.data.repository.MusicAppRepository
import javax.inject.Inject

class GetArtistUseCase @Inject constructor(
   private  val repository: MusicAppRepository
) {
    suspend operator fun invoke(query: String): ArtistModel? = repository.getArtist(query)
}