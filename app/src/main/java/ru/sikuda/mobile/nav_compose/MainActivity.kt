package ru.sikuda.mobile.nav_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sikuda.mobile.nav_compose.ui.theme.Nav_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Nav_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

sealed class Screen1(val route: String, @StringRes val resourceId: Int) {
    object Profile : Screen1("profile", R.string.profile)
    object FriendsList : Screen1("friendslist", R.string.friends_list)
}

val items = listOf(
    Screen1.Profile,
    Screen1.FriendsList,
)

@Composable
fun Greeting() {

//    val navController = rememberNavController()
//
//    val navhost = NavHost(
//        navController,
//        startDestination = "list"
//    ) {
//        composable("list") { List(navhost) }
//        composable("detail") { Detail(navhost) }
//    }

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen1.Profile.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen1.Profile.route) { Profile(navController) }
            composable(Screen1.FriendsList.route) { FriendsList(navController) }
        }

    }

    @Composable
    fun List(navhost: NavHost) {
        Text(text = "Hello list!")
    }

    @Composable
    fun Detail() {
        Text(text = "Hello detail!")
    }
}

@Composable
fun FriendsList(navController: NavHostController) {
    Text(text = "Hello list!")
}

@Composable
fun Profile(navController: NavHostController) {
    Text(text = "Hello profile!")
}

