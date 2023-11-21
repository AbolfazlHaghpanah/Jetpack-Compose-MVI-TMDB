package com.example.tmdb.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tmdb.R
import com.example.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.example.tmdb.navigation.AppScreens

@Composable
fun TMDBBottomNavigation(
    navController: NavController,
    bottomBarState: Boolean = true
) {
    AnimatedVisibility(
        visible = bottomBarState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BottomNavigation(
            modifier = Modifier
                .navigationBarsPadding(),
            backgroundColor = TMDBTheme.colors.background,
            elevation = 24.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            bottomNavItems.forEach { item ->
                BottomNavigationItem(
                    modifier = Modifier
                        .fillMaxSize(),
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(
                                navController
                                    .graph
                                    .findStartDestination().route!!
                            ) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label,
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.caption
                        )
                    },
                    selectedContentColor = TMDBTheme.colors.primary,
                    unselectedContentColor = TMDBTheme.colors.gray,
                )

            }

        }
    }
}


sealed class BottomNavigationItems(
    val label: String,
    @DrawableRes val icon: Int,
    val route: String
) {
    object Home :
        BottomNavigationItems("Home", R.drawable.home, AppScreens.Home.route)

    object Search :
        BottomNavigationItems("Search", R.drawable.search, AppScreens.Search.route)

    object Favorite : BottomNavigationItems("Favorite", R.drawable.heart, AppScreens.Favorite.route)

}

val bottomNavItems = listOf(
    BottomNavigationItems.Home,
    BottomNavigationItems.Search,
    BottomNavigationItems.Favorite
)