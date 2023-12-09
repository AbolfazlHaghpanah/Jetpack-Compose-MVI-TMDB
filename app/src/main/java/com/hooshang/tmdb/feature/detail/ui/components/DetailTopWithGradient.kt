package com.hooshang.tmdb.feature.detail.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.hooshang.tmdb.R
import com.hooshang.tmdb.core.ui.component.TextIcon
import com.hooshang.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.hooshang.tmdb.core.utils.imageUrl
import com.hooshang.tmdb.feature.detail.domain.model.MovieDetailDomainModel
import java.math.RoundingMode

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBar(onBackArrowClick, detailsState, onFavoriteIconClick)

            Row(
                modifier = Modifier.padding(top = 30.dp, bottom = 50.dp)
            ) {
                ForegroundImage(detailsState.posterPath)
            }

            MovieInfo(detailsState)

            Text(
                text = stringResource(R.string.overview),
                color = TMDBTheme.colors.white,
                style = TMDBTheme.typography.subtitle1,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp, top = 24.dp, start = 24.dp)
            )
        }
    }
}

@Composable
private fun ForegroundImage(movieDetailPosterPath: String) {
    AsyncImage(
        model = "$imageUrl${movieDetailPosterPath}",
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .padding(start = 85.dp, end = 85.dp)
            .clip(TMDBTheme.shapes.medium),
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.videoimageerror)
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
        model = "$imageUrl${movieDetailPosterPath}",
        contentDescription = null,
        Modifier
            .fillMaxSize()
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient)
                }
            },
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.videoimageerror)
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
            text = "${movieDetailDomainModel.runtime} Minutes"
        )
        if (movieDetailDomainModel.genres.isNotEmpty()) {
            Divider(
                color = TMDBTheme.colors.gray,
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .align(Alignment.CenterVertically)
            )
            TextIcon(
                iconId = TMDBTheme.icons.film,
                text = movieDetailDomainModel.genres[0].second
            )
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp)
    ) {
        val roundedVote =
            movieDetailDomainModel.voteAverage.toBigDecimal()
                .setScale(1, RoundingMode.FLOOR)?.toDouble()

        TextIcon(
            text = roundedVote.toString(),
            iconId = TMDBTheme.icons.star,
            iconColor = TMDBTheme.colors.secondary,
            textColor = TMDBTheme.colors.secondary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TopBar(
    onBackArrowClick: () -> Unit,
    movieDetailDomainModel: MovieDetailDomainModel,
    onFavoriteIconClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    if (showDialog) {
        ShareDialog(
            movieDetailDomainModel.externalIds,
            movieDetailDomainModel.id,
            movieDetailDomainModel.title,
            { requestString ->
                uriHandler.openUri(uri = requestString)
            }
        ) {
            showDialog = it
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        TMDBIconButton(
            onClick = onBackArrowClick,
            Modifier
                .clip(TMDBTheme.shapes.rounded)
                .background(TMDBTheme.colors.surface)
                .align(Alignment.CenterStart),
        ) {
            IconWrapper(
                icon = TMDBTheme.icons.arrowBack,
                tintColor = TMDBTheme.colors.white
            )
        }

        Text(
            text = movieDetailDomainModel.title,
            style = TMDBTheme.typography.subtitle1,
            color = TMDBTheme.colors.white,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(50.dp, 200.dp)
                .basicMarquee(),
            maxLines = 1
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suitableIcon =
                if (!movieDetailDomainModel.isFavorite) TMDBTheme.icons.heartBorder else TMDBTheme.icons.heart
            TMDBIconButton(
                onClick = onFavoriteIconClick,
                Modifier
                    .clip(TMDBTheme.shapes.rounded)
                    .background(TMDBTheme.colors.surface),
            ) {
                IconWrapper(
                    icon = suitableIcon,
                    tintColor = TMDBTheme.colors.error
                )
            }

            TMDBIconButton(
                onClick = { showDialog = true },
                Modifier
                    .clip(TMDBTheme.shapes.rounded)
                    .background(TMDBTheme.colors.surface),
            ) {
                IconWrapper(
                    icon = TMDBTheme.icons.share,
                    tintColor = TMDBTheme.colors.primary
                )
            }
        }
    }
}

@Composable
private fun IconWrapper(
    @DrawableRes icon: Int,
    tintColor: Color
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = icon),
        contentDescription = null,
        tint = tintColor
    )
}

@Composable
private fun ShareDialog(
    externalIds: List<String>,
    movieId: Int?,
    movieTitle: String?,
    shareIconOnClock: (String) -> Unit,
    changeShowDialog: (Boolean) -> Unit
) {
    val imdbIndex = 0
    val instagramIndex = 1
    val twitterIndex = 2

    val isInstagramIdNotNull =
        externalIds[instagramIndex] != stringResource(R.string.null_text)
    val isTwitterIdNotNull =
        externalIds[twitterIndex] != stringResource(R.string.null_text)
    val isIMDBIdNotNull =
        externalIds[imdbIndex] != stringResource(R.string.null_text)

    Dialog(
        onDismissRequest = { changeShowDialog(false) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(TMDBTheme.shapes.large)
                .background(TMDBTheme.colors.surface)
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .padding(top = 12.dp)
        ) {
            TMDBIconButton(
                onClick = { changeShowDialog(false) },
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(TMDBTheme.icons.close),
                    contentDescription = "close",
                    tint = TMDBTheme.colors.gray
                )
            }
            Text(
                text = stringResource(R.string.open_in),
                style = TMDBTheme.typography.h6,
                color = TMDBTheme.colors.white,
                modifier = Modifier.padding(
                    bottom = 15.dp
                )
            )

            Divider(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                color = TMDBTheme.colors.background
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                TMDBIconButton(
                    onClick = {
                        shareIconOnClock("https://www.instagram.com/thegodfathermovie/${externalIds[instagramIndex]}")
                    },
                    enabled = isInstagramIdNotNull
                ) {
                    ImageWrapper(
                        shouldNotHaveAlpha = isInstagramIdNotNull,
                        image = R.drawable.instagram,
                        contentDescription = "share instagram link"
                    )
                }
                TMDBIconButton(
                    onClick = {
                        shareIconOnClock("https://twitter.com/${externalIds[twitterIndex]}")
                    },
                    enabled = isTwitterIdNotNull
                ) {
                    ImageWrapper(
                        shouldNotHaveAlpha = isTwitterIdNotNull,
                        image = R.drawable.twitter,
                        contentDescription = "share twitter link"
                    )
                }
                TMDBIconButton(
                    onClick = {
                        shareIconOnClock("https://www.imdb.com/title/${externalIds[imdbIndex]}")
                    },
                    enabled = isIMDBIdNotNull
                ) {
                    ImageWrapper(
                        shouldNotHaveAlpha = isIMDBIdNotNull,
                        image = R.drawable.imdb,
                        contentDescription = "share IMDB link"
                    )
                }
                TMDBIconButton(onClick = {
                    val titleSplit = movieTitle?.split(" ")
                    val titleSplitPlusDash = titleSplit?.joinToString(separator = "-") { it }
                    if (titleSplitPlusDash != null) {
                        shareIconOnClock("https://www.themoviedb.org/collection/${movieId}-${titleSplitPlusDash}")
                    }
                }) {
                    ImageWrapper(
                        shouldNotHaveAlpha = true,
                        image = R.drawable.tmdb,
                        contentDescription = "share TMDB link"
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageWrapper(
    shouldNotHaveAlpha: Boolean,
    @DrawableRes image: Int,
    contentDescription: String
) {
    Image(
        painter = painterResource(image),
        contentDescription = contentDescription,
        alpha = if (shouldNotHaveAlpha) 1f else 0.3f
    )
}

@Composable
fun TMDBIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}