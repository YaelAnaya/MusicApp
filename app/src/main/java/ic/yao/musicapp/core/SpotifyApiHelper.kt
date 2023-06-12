package ic.yao.musicapp.core

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.spotifyAppApi

object SpotifyApiHelper {
    private const val clientId = "5bb1a13c361d4ab680c3436dc16ddb8d"
    private const val clientSecret = "1413069ccf8e45d8b6f810b027393e25"
    private val api: SpotifyAppApi? = null

    /**
     * Se sigue el patrón de diseño Singleton para evitar crear múltiples
     * instancias del objeto que se comunica con la API de Spotify.
     * https://en.wikipedia.org/wiki/Singleton_pattern
     * */
    suspend fun getApi(): SpotifyAppApi = api ?: spotifyAppApi(clientId, clientSecret).build()
}