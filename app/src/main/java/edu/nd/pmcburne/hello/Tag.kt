package edu.nd.pmcburne.hello

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index


@Entity(tableName = "tags", indices = [Index(value = ["placemarkId", "tagName"], unique = true)])
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val placemarkId: Int,
    val tagName: String
)

