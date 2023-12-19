package com.hooshang.tmdb.feature.favorite.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hooshang.tmdb.core.ui.theme.designsystem.Theme

@Composable
fun TrashIcon() {
    Image(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 16.dp),
        painter = painterResource(id = Theme.icons.illustration),
        contentDescription = null
    )
}