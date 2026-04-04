package edu.nd.pmcburne.hello

import kotlinx.serialization.Serializable

@Serializable
data class VisualCenterDTO(
    val latitude: Double,
    val longitude: Double,
)
