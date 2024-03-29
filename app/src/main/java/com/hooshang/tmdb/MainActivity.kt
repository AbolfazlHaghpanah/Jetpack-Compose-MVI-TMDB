package com.hooshang.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.hooshang.tmdb.core.ui.component.BottomNavigationItems
import com.hooshang.tmdb.core.ui.component.TMDBBottomNavigation
import com.hooshang.tmdb.core.ui.component.TMDBSnackBar
import com.hooshang.tmdb.core.ui.theme.TMDBTheme
import com.hooshang.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.hooshang.tmdb.core.utils.SnackBarManager
import com.hooshang.tmdb.navigation.AppScreens
import com.hooshang.tmdb.navigation.mainNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackBarManager: SnackBarManager

    @OptIn(
        ExperimentalMaterialNavigationApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            )
        )

        setContent {
            val bottomSheetNavigatorState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Expanded,
                skipHalfExpanded = true
            )
            val bottomSheetNavigator = remember {
                BottomSheetNavigator(bottomSheetNavigatorState)
            }
            val snackBarHostState = remember {
                SnackbarHostState()
            }
            var shouldShowBottomBar by remember {
                mutableStateOf(true)
            }
            val navController = rememberNavController(bottomSheetNavigator)
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val scaffoldState = rememberScaffoldState()
            val context = LocalContext.current

            DisposableEffect(navController) {
                val onDesChangeCheckBottomBarShow =
                    NavController.OnDestinationChangedListener { _, destination, _ ->
                        shouldShowBottomBar = destination.route != AppScreens.Detail.route
                    }
                val onDesChangeDismissSnackBar =
                    NavController.OnDestinationChangedListener { _, _, _ ->
                        snackBarHostState.currentSnackbarData?.dismiss()
                    }

                navController.addOnDestinationChangedListener(onDesChangeCheckBottomBarShow)
                navController.addOnDestinationChangedListener(onDesChangeDismissSnackBar)

                onDispose {
                    navController.removeOnDestinationChangedListener(onDesChangeDismissSnackBar)
                    navController.removeOnDestinationChangedListener(onDesChangeCheckBottomBarShow)
                }
            }

            LaunchedEffect(Unit) {
                snackBarManager.snackBarMessage.collectLatest { snackBarMessage ->
                    if (snackBarMessage?.snackBarMessage.isNullOrEmpty().not()) {
                        val snackBarResult = snackBarHostState.showSnackbar(
                            message = snackBarMessage?.snackBarMessage!!,
                            actionLabel = snackBarMessage.snackBarActionLabel?.asString(context = context),
                            duration = snackBarMessage.snackBarDuration
                        )
                        if (snackBarResult == SnackbarResult.ActionPerformed) {
                            snackBarMessage.performAction()
                        }
                    }
                }
            }

            TMDBTheme {
                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    sheetShape = TMDBTheme.shapes.veryLarge.copy(
                        bottomStart = CornerSize(0),
                        bottomEnd = CornerSize(0)
                    ),
                    scrimColor = Color.Transparent
                ) {
                    Scaffold(
                        modifier = Modifier.imePadding(),
                        scaffoldState = scaffoldState,
                        bottomBar = {
                            AnimatedVisibility(
                                visible = shouldShowBottomBar,
                                enter = slideIn(tween(100)) {
                                    IntOffset(0, it.height)
                                },
                                exit = slideOut(tween(100)) {
                                    IntOffset(0, it.height)
                                }
                            ) {
                                TMDBBottomNavigation(
                                    items = listOf(
                                        BottomNavigationItems.Home,
                                        BottomNavigationItems.Search,
                                        BottomNavigationItems.Favorite
                                    ),
                                    isSelected = { itemRoute ->
                                        navBackStackEntry?.destination?.hierarchy?.any { it.route == itemRoute } == true
                                    },
                                    onNavItemClick = { route ->
                                        navController.navigate(route) {
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
                                    }
                                )
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(snackBarHostState) {
                                TMDBSnackBar(
                                    message = it.message,
                                    actionLabel = it.actionLabel,
                                    performAction = {
                                        it.performAction()
                                    }
                                )
                            }
                        }
                    ) {
                        NavHost(
                            modifier = Modifier
                                .padding(
                                    top = it.calculateTopPadding(),
                                    bottom = if (shouldShowBottomBar) {
                                        it.calculateBottomPadding()
                                    } else {
                                        0.dp
                                    },
                                    start = it.calculateStartPadding(LayoutDirection.Ltr),
                                    end = it.calculateEndPadding(LayoutDirection.Ltr)
                                )
                                .fillMaxSize(),
                            navController = navController,
                            startDestination = AppScreens.Home.route,
                        ) {
                            mainNavGraph(navController)
                        }
                    }
                }
            }
        }
    }
}
