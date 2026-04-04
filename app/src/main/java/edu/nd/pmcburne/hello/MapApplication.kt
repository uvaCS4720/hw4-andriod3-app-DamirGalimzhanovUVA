package edu.nd.pmcburne.hello

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MapApplication : Application() {

    lateinit var db: AppDatabase
    lateinit var api: ApiService
    lateinit var placemark: PlacemarkDao
    lateinit var tag: TagDao

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        ignoreUnknownKeys = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }

    override fun onCreate(){
        super.onCreate()

        api = Retrofit.Builder()
            .baseUrl("https://www.cs.virginia.edu/")
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(ApiService::class.java)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "placemarks-database"
        ).build()

        runBlocking {
            val placemarkDTOs = api.getPlacemarks()
            for (placemarkDTO in placemarkDTOs) {
                val placemark = Placemark(
                    placemarkDTO.id,
                    placemarkDTO.name,
                    placemarkDTO.description,
                    placemarkDTO.visualCenter.latitude,
                    placemarkDTO.visualCenter.longitude,
                )
                db.dao().insertPlacemark(placemark)
                for (tagDTO in placemarkDTO.tagList) {
                    val tag = Tag(
                        placemarkId = placemarkDTO.id,
                        tagName = tagDTO
                    )
                    db.dao().insertTag(tag)
                }
            }
            println(db.dao().getTags())
        }

    }



}