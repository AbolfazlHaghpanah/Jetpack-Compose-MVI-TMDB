package com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence

import androidx.room.Entity

@Entity(primaryKeys = ["id", "genreId"])
data class MovieWithGenreCrossRef(
    val id: Int,
    val genreId: Int
)
