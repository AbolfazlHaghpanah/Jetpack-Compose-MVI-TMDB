package com.example.tmdb.feature.detail.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdb.R
import com.example.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.example.tmdb.core.utils.imageUrl
import com.example.tmdb.feature.detail.data.relation.DetailMovieWithAllRelations
import com.example.tmdb.feature.detail.data.credit.CreditEntity

@Composable
fun OverviewContentWithCastAndCrew(movieDetail: DetailMovieWithAllRelations) {
    Text(
        text = movieDetail.detailEntity.overview,
        color = TMDBTheme.colors.whiteGray,
        style = TMDBTheme.typography.subtitle2,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    Text(
        text = stringResource(R.string.cast_and_crew),
        style = TMDBTheme.typography.subtitle1,
        color = TMDBTheme.colors.white,
        modifier = Modifier
            .padding(
                top = 24.dp,
                start = 24.dp
            )
    )
    val castAndCrewCombinedList =
        movieDetail.credits.toMutableList()
    if (castAndCrewCombinedList.size > 0) {
        CastCrewLazyRow(castAndCrewCombinedList)
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun CastCrewLazyRow(
    castOrCrewElements: List<CreditEntity>
) {

    LazyRow(
        contentPadding = PaddingValues(
            start = 24.dp,
            top = 16.dp
        )
    ) {
        items(castOrCrewElements) { castOrCrew ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CreditImageWrapper(castOrCrew.profilePath)
                Column(
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = castOrCrew.name,
                        style = TMDBTheme.typography.body1,
                        color = TMDBTheme.colors.white,
                        modifier = Modifier
                            .widthIn(60.dp, 112.dp)
                            .basicMarquee()
                    )
                    Text(
                        text = castOrCrew.job ?: stringResource(R.string.actor),
                        style = TMDBTheme.typography.overLine,
                        color = TMDBTheme.colors.gray
                    )
                }
            }
        }
    }
}

@Composable
private fun CreditImageWrapper(castOrCrewProfilePath: String?) {
    AsyncImage(
        model = "$imageUrl${castOrCrewProfilePath}",
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(40.dp)
            .clip(TMDBTheme.shapes.rounded),
        error = painterResource(id = R.drawable.profileerror)
    )
}