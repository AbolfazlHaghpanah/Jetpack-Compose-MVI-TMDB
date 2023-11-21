package com.example.tmdb.feature.detail.data.crossrefrence

import androidx.room.Entity

@Entity(primaryKeys = ["detailMovieId", "genreId"])
data class DetailMovieWithGenreCrossRef(
    val detailMovieId: Int,
    val genreId: Int
)