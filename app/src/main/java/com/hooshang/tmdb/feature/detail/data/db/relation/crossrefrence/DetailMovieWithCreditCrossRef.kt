package com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence

import androidx.room.Entity

@Entity(primaryKeys = ["detailMovieId", "creditId"])
data class DetailMovieWithCreditCrossRef(
    val detailMovieId: Int,
    val creditId: Int
)