package ic.yao.musicapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ic.yao.musicapp.navigation.BottomNavigationGraph
import ic.yao.musicapp.ui.artist.ArtistViewModel
import ic.yao.musicapp.ui.theme.PrettyBlue
import ic.yao.musicapp.util.BottomGraph
import ic.yao.musicapp.util.bottomNavScreens
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: ArtistViewModel,
    goBack : () -> Unit,
    navController: NavHostController = rememberNavController(),
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold (
        bottomBar = {
            BottomBar(
                screens = bottomNavScreens,
                navController = navController,
                currentDestination = currentDestination
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ){
        Surface(
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // just show the top bar when the current destination is in the bottom nav graph
                if (currentDestination?.hierarchy?.any { it.route in bottomNavScreens.map { screen -> screen.route } } == true) {
                    TopBar(
                        goBack = goBack,
                        screens = bottomNavScreens,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
                BottomNavigationGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    goBack : () -> Unit,
    screens: List<BottomGraph> = emptyList(),
    currentDestination: NavDestination? = null,
    navController: NavController
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text =
                screens.find { screen ->
                    currentDestination?.hierarchy?.any { destination ->
                        destination.route == screen.route
                    } == true
                }?.title?.uppercase() ?: "",
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = {
                when (currentDestination?.route) {
                    BottomGraph.HomeScreen.route -> goBack()
                    else -> navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = "Navigation Icon"
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    )
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    screens: List<BottomGraph> = emptyList(),
    currentDestination: NavDestination? = null
) {
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar(
            modifier = modifier
                .clip(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            containerColor = PrettyBlue,
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }

        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomGraph,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = {
            Icon(
                imageVector = screen.icon ?: Icons.Rounded.Home,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFFF7FFF8),
            unselectedIconColor = Color(0xFFB2C9DB),
            selectedTextColor = Color(0xFFF7FFF8),
            unselectedTextColor = Color(0xFFB2C6DB),
            indicatorColor = PrettyBlue
        ),
    )
}