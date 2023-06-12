package ic.yao.musicapp.ui.albums

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Explicit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ic.yao.musicapp.R
import ic.yao.musicapp.data.model.AlbumModel
import ic.yao.musicapp.data.model.ArtistModel
import ic.yao.musicapp.data.model.TrackModel
import ic.yao.musicapp.ui.artist.AlbumsList
import ic.yao.musicapp.ui.artist.ArtistDetailHeader
import ic.yao.musicapp.ui.artist.ArtistDetailTopBar
import ic.yao.musicapp.ui.artist.LabeledText
import ic.yao.musicapp.ui.artist.TOP_BAR_HEIGHT
import ic.yao.musicapp.ui.artist.isScrolled
import ic.yao.musicapp.util.getFormattedTrackDuration

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlbumDetailScreen(
    navController: NavHostController,
    albumModel: AlbumModel?,
) {
    var album by remember { mutableStateOf<AlbumModel?>(albumModel) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = albumModel) {
        albumModel?.let {
            album = albumModel
        }
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AlbumDetailTopBar(
                album = album,
                lazyListState = lazyListState,
                goBack = { navController.popBackStack() },
            )
            TrackList(
                tracks = album?.tracks ?: listOf(),
                lazyListState = lazyListState,
            )
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailTopBar(
    modifier: Modifier = Modifier,
    album: AlbumModel?,
    lazyListState: LazyListState,
    goBack : () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .animateContentSize(animationSpec = tween(300))
            .height(height = if (lazyListState.isScrolled) 64.dp else 430.dp),
    ) {
        CenterAlignedTopAppBar(
            title = {
                AnimatedVisibility(
                    visible = lazyListState.isScrolled, enter = fadeIn(
                        animationSpec = tween(300)
                    ), exit = fadeOut(animationSpec = tween(300))
                ) {
                    Text(
                        text = album?.name ?: "",
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(0.6f)
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

        if (!lazyListState.isScrolled) {
            AlbumDetailHeader(album = album)
        }

    }
}

@Composable
fun TrackList(
    tracks: List<TrackModel>,
    lazyListState: LazyListState,
){
    var showTrackInformation by remember { mutableStateOf(false) }
    var currentTrack by remember { mutableStateOf<TrackModel?>(null) }
    val listPadding by animateDpAsState(
        targetValue = if (lazyListState.isScrolled) 0.dp else 2.dp,
        animationSpec = tween(250)
    )
    if (showTrackInformation) {
        TrackPreviewDialog(
            onDismissRequest = { showTrackInformation = false },
            track = currentTrack
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = listPadding),
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(tracks.size ) {
            tracks[it].let { track ->
                TrackItem(
                    track = track,
                    onCLick = {
                        currentTrack = track
                        showTrackInformation = true
                    }
                )
            }
        }
    }
}

@Composable
fun AlbumDetailHeader(
    album: AlbumModel?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .animateContentSize(animationSpec = tween(300)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp).background(Color.Transparent))
        AlbumCover(album = album)
        Spacer(modifier = Modifier.height(36.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = album?.recordType ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = album?.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${album?.artist} • ${album?.releaseYear}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 10.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 0.9f.dp
        )
    }
}

@Composable
fun TrackItem(
    track: TrackModel,
    onCLick : () -> Unit = {}
) {
    Column(
        modifier =  Modifier.clickable { onCLick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = track.trackNumber.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (track.explicit) {
                        Icon(
                            imageVector = Icons.Rounded.Explicit,
                            contentDescription = "Explicit",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(17.dp)
                                .padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = track.artists.joinToString(", "),
                        style = MaterialTheme.typography.titleMedium,
                        lineHeight = 0.sp,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 2.dp),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Text(
                text = getFormattedTrackDuration(track.duration),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 11.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 0.9.dp
        )
    }

}


@Composable
fun AlbumCover(
    album: AlbumModel?
) {
    ElevatedCard(
        modifier = Modifier
            .size(230.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 15.dp
        ),
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = album!!.imageURL)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                    }).build()
            ),
            contentScale = ContentScale.FillHeight,
            contentDescription = "Album Image",
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackPreviewDialog(
    onDismissRequest: () -> Unit,
    track: TrackModel?
){
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        )
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(270.dp)
                .clip(MaterialTheme.shapes.large),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Track Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(horizontal = 22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    LabeledText(title = "Album", value = track!!.album, fontSize = 14.sp)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 0.9.dp
                    )
                    LabeledText(title = "Name", value = track.name, fontSize = 14.sp)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 0.9.dp
                    )
                    LabeledText(title = "Performed by", value = track.artists.joinToString(", "), maxLines = 2, fontSize = 14.sp)
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 0.9.dp
                    )
                    LabeledText(title = "Duration", value = getFormattedTrackDuration(track.duration), fontSize = 14.sp)
                }
            }
        )
    }

}
