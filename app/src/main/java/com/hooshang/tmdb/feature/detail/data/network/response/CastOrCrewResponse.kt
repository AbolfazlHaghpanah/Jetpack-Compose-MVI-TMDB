package com.hooshang.tmdb.feature.detail.data.network.response

import androidx.annotation.Keep
import com.hooshang.tmdb.feature.detail.data.db.entity.CreditEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CastOrCrewResponse(
    val id: Int,
    val name: String,
    @SerialName("profile_path")
    val profilePath: String?,
    val job: String? = null
) {
    fun toCreditEntity(): CreditEntity = CreditEntity(
        creditId = id,
        name = name,
        job = job ?: "Actor",
        profilePath = profilePath
    )
}