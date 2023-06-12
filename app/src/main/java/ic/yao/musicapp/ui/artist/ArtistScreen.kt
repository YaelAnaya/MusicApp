package ic.yao.musicapp.ui.artist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ic.yao.musicapp.R
import ic.yao.musicapp.data.model.ArtistModel
import ic.yao.musicapp.ui.theme.PrettyBlue
import ic.yao.musicapp.util.DetailGraph


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistScreen(
    viewModel: ArtistViewModel = hiltViewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.observeAsState()

    LaunchedEffect(key1 = state!!.isSearching) {
        if (state!!.searchText.isNotBlank() && state!!.isSearching) {
            viewModel.onEvent(ArtistViewModel.SearchEvent.Search(state!!.searchText))
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArtistSearchBar(state, viewModel)
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 15.dp, bottom = 72.dp),
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(state!!.artists.size) { index ->
                ArtistCard(
                    modifier = Modifier.animateItemPlacement(tween(800)),
                    artist = state!!.artists[index],
                    onClick = {
                        navController.navigate(DetailGraph.ArtistDetailScreen.passArguments(state!!.artists[index]!!.name))
                    },
                    onFavorite = {
                        viewModel.onFavorite(it)
                    }
                )
            }
        }
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ArtistSearchBar(
    state: ArtistViewModel.ArtistState?,
    viewModel: ArtistViewModel
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth(),
        query = state!!.searchText,
        onQueryChange = {
            viewModel.onEvent(ArtistViewModel.SearchEvent.QueryChange(it))
        },
        onActiveChange = {
            viewModel.onEvent(ArtistViewModel.SearchEvent.SearchActiveChange(it))
        },
        onSearch = {
            viewModel.onEvent(ArtistViewModel.SearchEvent.Search(it))
        },
        active = state.isSearching,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = state.isSearching,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(ArtistViewModel.SearchEvent.ClearSearch)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear Search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "Search",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            inputFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
    ) {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistCard(
    modifier: Modifier,
    artist: ArtistModel?,
    onClick: () -> Unit,
    onFavorite : (ArtistModel) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
            },
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = artist!!.imageURL)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.ic_launcher_background)
                            }).build()
                    ),
                    contentDescription = "Album Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(90.dp))
                )
            Spacer(modifier = Modifier.size(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = artist.name ,
                            fontSize = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                        )
                        Icon(
                            modifier = Modifier
                                .size(18.dp)
                                .padding(bottom = 4.dp),
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = "Verified Icon",
                            tint = Color(0xFF1976D2)
                        )
                    }
                    LabeledText(
                        title = "Genre",
                        value = artist.genre.capitalize(Locale.current)
                    )

                }
                FavoriteButton(
                    isFavorite = artist.isFavorite,
                    onClick = {
                        onFavorite(artist)
                    }
                )
            }
        }
    }
}

@Composable fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onClick() }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            contentDescription = "Favorite",
            tint = PrettyBlue
        )
    }
}

@Composable
fun LabeledText(
    title: String,
    value: String,
    delimiter: String = ": ",
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1,
    fontSize : TextUnit = 12.sp,
    color : Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                append("$title$delimiter")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            ) {
                append(value)
            }
        },
        maxLines = maxLines,
        overflow = overflow,
    )
}