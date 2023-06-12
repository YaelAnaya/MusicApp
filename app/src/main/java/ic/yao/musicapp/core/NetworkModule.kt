package ic.yao.musicapp.core

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.spotifyAppApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val clientId = "5bb1a13c361d4ab680c3436dc16ddb8d"
    private const val clientSecret = "1413069ccf8e45d8b6f810b027393e25"

    @Singleton
    @Provides
    suspend fun provideSpotifyAppApi(): SpotifyAppApi = spotifyAppApi(clientId, clientSecret).build()
}