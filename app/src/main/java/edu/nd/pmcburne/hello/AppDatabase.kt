package edu.nd.pmcburne.hello

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Placemark::class, Tag::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun dao(): PlacemarkDao

//    abstract fun tagDao(): TagDao
}