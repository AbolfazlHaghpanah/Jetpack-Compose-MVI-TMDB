package com.hooshang.tmdb.core.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val genreId: Int,
    val genreName: String
)