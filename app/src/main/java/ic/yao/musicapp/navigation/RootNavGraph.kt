package ic.yao.musicapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ic.yao.musicapp.ui.MainScreen
import ic.yao.musicapp.ui.artist.ArtistViewModel
import ic.yao.musicapp.ui.home.HomeScreen
import ic.yao.musicapp.util.Graph

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    viewModel: ArtistViewModel = hiltViewModel()
){

    NavHost(
        navController = navController,
        startDestination = Graph.RootGraph.route
    ){
        composable(route = Graph.RootGraph.route){
            HomeScreen(
                navigateTo = {
                    navController.navigate(Graph.BottomNavGraph.route)
                }
            )
        }
        composable(route = Graph.BottomNavGraph.route){
            MainScreen(
                goBack = {
                    navController.popBackStack()
                    viewModel.state.value!!.isSearching = false
                },
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }
    }
}

