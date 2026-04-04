package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlacemarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacemark(placemark: Placemark)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacemarks(placemarks: List<Placemark>)

    @Query("""SELECT distinct placemarks.id, name, description, latitude, longitude FROM placemarks
        JOIN tags ON placemarks.id = tags.placemarkId
        WHERE tagName IN (:tag)
    """
    )
    suspend fun getPlacemarksWithTag(tag: String) : List<Placemark>

    @Query("SELECT * FROM tags")
    suspend fun getTags() : List<Tag>

//    @Query("SELECT distinct placemarkId, tagName FROM tags")
//    suspend fun getDistinctTags() : List<DistinctTag>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<Tag>)

}