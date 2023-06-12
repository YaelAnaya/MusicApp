package ic.yao.musicapp.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgs
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ic.yao.musicapp.data.model.AlbumModel
import ic.yao.musicapp.ui.MainScreen
import ic.yao.musicapp.ui.albums.AlbumDetailScreen
import ic.yao.musicapp.ui.albums.AlbumDetailViewModel
import ic.yao.musicapp.ui.artist.ArtistDetailScreen
import ic.yao.musicapp.ui.artist.ArtistScreen
import ic.yao.musicapp.ui.artist.ArtistViewModel
import ic.yao.musicapp.ui.favorites.FavoritesScreen
import ic.yao.musicapp.ui.home.HomeScreen
import ic.yao.musicapp.util.BottomGraph
import ic.yao.musicapp.util.DetailGraph
import ic.yao.musicapp.util.Graph
import ic.yao.musicapp.util.parcelableTypeOf

@Composable
fun BottomNavigationGraph(
    viewModel: ArtistViewModel,
    modifier : Modifier = Modifier,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = BottomGraph.HomeScreen.route,
        modifier = modifier
    ){
        composable(route = BottomGraph.HomeScreen.route){
            ArtistScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(route = BottomGraph.FavoriteScreen.route){
            FavoritesScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        addGraph(navController = navController)
    }
}

fun NavGraphBuilder.addGraph(
    navController: NavHostController
){
    composable(
        route = DetailGraph.ArtistDetailScreen.route,
        arguments = listOf(
            navArgument("id"){
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }
        ),

    ){
        ArtistDetailScreen(
            navController = navController,
        )

    }
    composable(
        route = DetailGraph.AlbumDetailScreen.route,

    ){
        val album = navController.previousBackStackEntry?.arguments?.getParcelable<AlbumModel>("album")
        AlbumDetailScreen(navController = navController, albumModel = album)
    }
}

