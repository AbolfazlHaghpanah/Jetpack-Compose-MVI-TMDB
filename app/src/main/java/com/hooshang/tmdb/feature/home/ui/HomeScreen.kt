package com.hooshang.tmdb.feature.home.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hooshang.tmdb.R
import com.hooshang.tmdb.core.ui.component.MovieRow
import com.hooshang.tmdb.core.ui.shimmer.fakeMovie
import com.hooshang.tmdb.core.ui.shimmer.ifShimmerActive
import com.hooshang.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.hooshang.tmdb.feature.home.ui.component.PagerMovieItem
import com.hooshang.tmdb.feature.home.ui.component.TMDBPagerIndicator
import com.hooshang.tmdb.feature.home.ui.contracts.HomeAction
import com.hooshang.tmdb.feature.home.ui.contracts.HomeState
import com.hooshang.tmdb.navigation.AppScreens
import kotlinx.collections.immutable.toPersistentList

@NonRestartableComposable
@Composable
fun HomeScreen(
    navController: NavController
) {
    HomeScreen(
        navController = navController,
        viewModel = hiltViewModel()
    )
}

@NonRestartableComposable
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = 2)

    val onAction: (HomeAction) -> Unit = remember {
        { action ->
            when (action) {
                is HomeAction.NavigateToDetail ->
                    navController.navigate(AppScreens.Detail.createRoute(action.id)) {
                        popUpTo(AppScreens.Home.route)
                        launchSingleTop = true
                    }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        onAction(HomeAction.ShowLastSnackBar)
    }

    HomeScreen(
        homeState = homeState,
        pagerState = pagerState,
        onAction = onAction,
    )
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreen(
    homeState: HomeState,
    pagerState: PagerState,
    onAction: (HomeAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.statusBarsPadding()
    ) {

        item {
            HorizontalPager(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(180.dp),
                state = pagerState,
                count = if (homeState.nowPlayingMovies.size > 5) 5 else fakeMovie.size,
                itemSpacing = 12.dp,
                contentPadding = PaddingValues(horizontal = 40.dp)
            ) { page ->

                val pagerSize = animateDpAsState(
                    targetValue = if (page == pagerState.currentPage) 180.dp else 160.dp,
                    label = ""
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    PagerMovieItem(
                        isLoading = homeState.nowPlayingMovies.size <= 5,
                        modifier = Modifier
                            .clip(TMDBTheme.shapes.large)
                            .clickable {
                                if (homeState.nowPlayingMovies.size >= 5) onAction(
                                    HomeAction.NavigateToDetail(id = homeState.nowPlayingMovies[page].movieId)
                                )
                            }
                            .height(pagerSize.value),
                        movie = if (homeState.nowPlayingMovies.size >= 5) {
                            homeState.nowPlayingMovies[page]
                        } else {
                            fakeMovie[0]
                        }
                    )
                }
            }
        }

        item {
            TMDBPagerIndicator(
                modifier = Modifier
                    .ifShimmerActive(homeState.nowPlayingMovies.isEmpty()),
                pageCount = pagerState.pageCount,
                selectedPage = pagerState.currentPage
            )
        }

        item {
            MovieRow(
                onClick = { id ->
                    onAction(HomeAction.NavigateToDetail(id = id))
                },
                title = stringResource(R.string.most_popular),
                movies = homeState.popularMovies.toPersistentList()
            )
        }

        item {
            MovieRow(
                onClick = { id ->
                    onAction(HomeAction.NavigateToDetail(id = id))
                },
                title = stringResource(R.string.top_rated),
                movies = homeState.topRatedMovies.toPersistentList()
            )
        }
    }
}







