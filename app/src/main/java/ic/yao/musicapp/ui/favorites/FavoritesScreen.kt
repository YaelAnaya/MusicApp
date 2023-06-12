package ic.yao.musicapp.ui.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ic.yao.musicapp.data.model.ArtistModel
import ic.yao.musicapp.ui.artist.ArtistCard
import ic.yao.musicapp.ui.artist.ArtistViewModel
import ic.yao.musicapp.util.DetailGraph

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    viewModel: ArtistViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state by viewModel.state.observeAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp, bottom = 72.dp),
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(state!!.favoriteArtists.size) { index ->
            AnimatedVisibility(visible = state!!.favoriteArtists[index]!!.isFavorite) {
                ArtistCard(
                    modifier = Modifier.animateItemPlacement(tween(300)),
                    artist = state!!.favoriteArtists[index],
                    onClick = {
                        navController.navigate(DetailGraph.ArtistDetailScreen.passArguments(state!!.favoriteArtists[index]!!.name))
                    },
                    onFavorite = {
                        viewModel.onFavorite(it)
                    }
                )
            }
        }
    }

}