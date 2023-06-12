package ic.yao.musicapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ic.yao.musicapp.data.database.MusicAppDatabase
import ic.yao.musicapp.data.database.dao.ArtistDao
import ic.yao.musicapp.data.repository.MusicAppRepository
import ic.yao.musicapp.network.SpotifyService
import ic.yao.musicapp.util.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMusicAppDatabase(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        MusicAppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideArtistDao(
        db: MusicAppDatabase
    ) = db.artistDao()

    @Provides
    @Singleton
    fun provideMusicAppRepository(
        dao: ArtistDao,
        api: SpotifyService
    ) = MusicAppRepository(api, dao)
}