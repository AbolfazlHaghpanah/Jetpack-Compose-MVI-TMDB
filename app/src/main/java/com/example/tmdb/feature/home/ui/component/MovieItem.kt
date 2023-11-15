package com.example.tmdb.feature.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdb.core.ui.component.VoteIcon
import com.example.tmdb.core.ui.shimmer.ifShimmerActive
import com.example.tmdb.core.ui.theme.designsystem.TMDBTheme
import com.example.tmdb.core.utils.imageUrl

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    isShimmer: Boolean = false,
    onClick: () -> Unit,
    title: String,
    image: String,
    genres: String,
    vote: String
) {
    Column(
        modifier = modifier
            .clip(TMDBTheme.shapes.medium)
            .clickable(onClick = onClick)
            .background(
                TMDBTheme.colors.surface,
                TMDBTheme.shapes.medium
            )
            .width(140.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp)
        ) {

            VoteIcon(
                modifier = Modifier
                    .ifShimmerActive(isShimmer),
                vote = vote
            )

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = imageUrl + image,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 12.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
                .ifShimmerActive(isShimmer),
            text = title,
            style = TMDBTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .ifShimmerActive(isShimmer),
            text = genres,
            style = TMDBTheme.typography.overLine,
            maxLines = 1,
            color = TMDBTheme.colors.gray,
            overflow = TextOverflow.Ellipsis
        )
    }
}