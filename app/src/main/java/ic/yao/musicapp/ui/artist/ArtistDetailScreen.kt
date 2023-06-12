package ic.yao.musicapp.ui.artist

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ic.yao.musicapp.data.model.ArtistModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ic.yao.musicapp.R
import ic.yao.musicapp.data.model.AlbumModel
import ic.yao.musicapp.ui.common.LoadingAnimation
import ic.yao.musicapp.ui.theme.PrettyBlue
import ic.yao.musicapp.util.DetailGraph
import ic.yao.musicapp.util.getFormattedDuration

val TOP_BAR_HEIGHT = 370.dp
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArtistDetailScreen(
    navController: NavHostController,
    viewModel: ArtistViewModel = hiltViewModel()
) {
    val artistId = navController.currentBackStackEntry?.arguments?.getString("id")
    var artist by remember { mutableStateOf<ArtistModel?>(null) }
    var albums by remember { mutableStateOf<List<AlbumModel>>(listOf()) }
    var isLoading by remember { mutableStateOf(true) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = artistId) {
        artistId?.let {
            isLoading = true
            artist = viewModel.searchArtist(it)
            albums = viewModel.searchAlbums(it)
            isLoading = false
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ArtistDetailTopBar(
                artist = artist,
                lazyListState = lazyListState,
                goBack = { navController.popBackStack() },
            )
            AlbumsList(
                albums = albums,
                navController = navController,
                lazyListState = lazyListState,
                isLoading = isLoading,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailTopBar(
    modifier: Modifier = Modifier,
    artist: ArtistModel?,
    lazyListState: LazyListState,
    goBack : () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .animateContentSize(animationSpec = tween(300))
            .height(height = if (lazyListState.isScrolled) 64.dp else TOP_BAR_HEIGHT),
    ){
        CenterAlignedTopAppBar(
            title = {
                AnimatedVisibility(visible = lazyListState.isScrolled, enter = fadeIn(
                    animationSpec = tween(300)
                ), exit = fadeOut(animationSpec = tween(300))) {
                    Text(
                        text = artist?.name ?: "",
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.SemiBold
                        )
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    goBack()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronLeft,
                        contentDescription = "Navigation Icon",
                        tint = if (lazyListState.isScrolled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = if (lazyListState.isScrolled) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            )
        )
        if( !lazyListState.isScrolled) {
            ArtistDetailHeader(artist = artist, modifier = modifier)
        }

    }
}

@Composable
fun AlbumsList(
    modifier: Modifier = Modifier,
    albums: List<AlbumModel>,
    navController: NavHostController,
    lazyListState: LazyListState,
    isLoading : Boolean
) {
    val listPadding by animateDpAsState(
        targetValue = if (lazyListState.isScrolled) 0.dp else 2.dp,
        animationSpec = tween(250)
    )
    val loadingPadding by animateDpAsState(
        targetValue = if (isLoading) 140.dp else 0.dp,
        animationSpec = tween(250)
    )
    LazyColumn(
        modifier = Modifier.padding(top = listPadding),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item{
            Column {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 33.dp, vertical = 13.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    text = "Discography".uppercase(),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(loadingPadding))
                AnimatedVisibility(modifier = Modifier.align(Alignment.CenterHorizontally),visible = isLoading) {
                    LoadingAnimation()
                }
            }
        }
        items(albums.size) { index ->
            AnimatedVisibility(visible = !isLoading, enter = fadeIn()) {
                AlbumItem(
                    modifier = modifier,
                    album = albums[index],
                    onClick = { albumModel ->
                        navController.currentBackStackEntry?.arguments?.putParcelable(
                            "album",
                            albumModel
                        )
                        navController.navigate(DetailGraph.AlbumDetailScreen.route)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumItem(
    modifier: Modifier,
    album: AlbumModel,
    onClick: (AlbumModel) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 20.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = {
            onClick(album)
        },
    ) {
        Row(
            modifier = modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = album.imageURL)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_background)
                        }).build()
                ),
                contentScale = ContentScale.FillHeight,
                contentDescription = "Album Image",
                modifier = modifier
                    .height(110.dp)
                    .padding(start = 13.dp)
                    .clip(MaterialTheme.shapes.large)
            )
            Spacer(modifier = Modifier.width(13.dp))
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = album.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    lineHeight = 16.sp,
                    maxLines = 2,
                )
                Spacer(modifier = modifier.height(10.dp))
                LabeledText(
                    title = album.artist,
                    value = album.releaseYear ?: " ",
                    delimiter = " • "
                )
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = getFormattedDuration(album.duration),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = modifier
                        .padding(end = 18.dp)
                        .align(Alignment.End)
                )
            }
        }
    }

}


@Composable
fun ArtistDetailHeader(
    modifier: Modifier,
    artist: ArtistModel?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .animateContentSize(animationSpec = tween(300)),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = artist?.imageURL ?: "")
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                    }).build()
            ),
            contentDescription = "Artist Image",
            modifier = modifier
                .padding(start = 10.dp)
                .size(225.dp)
                .clip(RoundedCornerShape(50))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 33.dp, vertical = 13.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = artist?.name ?: "",
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(bottom = 4.dp),
                    imageVector = Icons.Rounded.Verified,
                    contentDescription = "Verified Icon",
                    tint = Color(0xFF1976D2)
                )
            }
            LabeledText(
                title = "Genre",
                value = artist?.genre?.capitalize(Locale.current) ?: ""
            )
            LabeledText(
                title = "Followers",
                value = artist?.followers?.toString() ?: ""
            )
        }

    }
}