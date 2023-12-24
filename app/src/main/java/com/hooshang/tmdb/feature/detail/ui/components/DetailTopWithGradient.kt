package com.hooshang.tmdb.feature.detail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hooshang.tmdb.R
import com.hooshang.tmdb.core.ui.component.TextIcon
import com.hooshang.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.hooshang.tmdb.core.utils.image_url
import com.hooshang.tmdb.feature.detail.domain.model.MovieDetailDomainModel

@Composable
fun DetailTopWithGradient(
    detailsState: MovieDetailDomainModel,
    onBackArrowClick: () -> Unit,
    onFavoriteIconClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackgroundImage(detailsState.posterPath)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(
                movieDetailDomainModel = detailsState,
                onBackArrowClick = onBackArrowClick,
                onFavoriteIconClick = onFavoriteIconClick
            )

            ForegroundImage(
                modifier = Modifier.padding(top = 30.dp, bottom = 50.dp),
                movieDetailPosterPath = detailsState.posterPath
            )

            MovieInfo(detailsState)

            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp, top = 24.dp, start = 24.dp),
                text = stringResource(R.string.label_overview),
                color = TMDBTheme.colors.white,
                style = TMDBTheme.typography.subtitle1
            )
        }
    }
}

@Composable
private fun ForegroundImage(
    movieDetailPosterPath: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .padding(start = 85.dp, end = 85.dp)
            .clip(TMDBTheme.shapes.medium),
        model = "$image_url${movieDetailPosterPath}",
        contentDescription = null,
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.img_video_image_error)
    )
}

@Composable
private fun BackgroundImage(movieDetailPosterPath: String) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            TMDBTheme.colors.background.copy(alpha = 0.57f),
            TMDBTheme.colors.background.copy(alpha = 1f)
        )
    )

    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient)
                }
            },
        model = "$image_url${movieDetailPosterPath}",
        contentDescription = null,
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.img_video_image_error)
    )
}

@Composable
private fun MovieInfo(movieDetailDomainModel: MovieDetailDomainModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        TextIcon(
            iconId = TMDBTheme.icons.calendar,
            text = movieDetailDomainModel.releaseDate.split(
                "-"
            )[0]
        )

        Divider(
            color = TMDBTheme.colors.gray,
            modifier = Modifier
                .width(1.dp)
                .height(16.dp)
                .align(Alignment.CenterVertically)
        )

        TextIcon(
            iconId = TMDBTheme.icons.clock,
            text = "${movieDetailDomainModel.runtime} " + stringResource(R.string.label_minutes)
        )

        if (movieDetailDomainModel.genres.isNotEmpty()) {
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .align(Alignment.CenterVertically),
                color = TMDBTheme.colors.gray
            )

            TextIcon(
                iconId = TMDBTheme.icons.film,
                text = movieDetailDomainModel.genres[0].second
            )
        }
    }

    TextIcon(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp),
        text = movieDetailDomainModel.voteAverage.toString(),
        iconId = TMDBTheme.icons.star,
        iconColor = TMDBTheme.colors.secondary,
        textColor = TMDBTheme.colors.secondary
    )
}

