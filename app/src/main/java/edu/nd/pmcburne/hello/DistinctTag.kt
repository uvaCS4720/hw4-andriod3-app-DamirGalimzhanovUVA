package edu.nd.pmcburne.hello

import androidx.room.Entity

@Entity
data class DistinctTag(
    val placemarkId: Int,
    val tagName: String
)
