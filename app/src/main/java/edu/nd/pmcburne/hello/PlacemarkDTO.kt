package edu.nd.pmcburne.hello

import kotlinx.serialization.Serializable

@Serializable
data class PlacemarkDTO(
    val id: Int,
    val name: String,
    val description: String,
    val tagList: List<String>,
    val visualCenter: VisualCenterDTO,
)
