package no.usn.mob3000.data.network.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import no.usn.mob3000.data.model.content.local.NewsItemLocal
import no.usn.mob3000.data.source.local.content.NewsDao

/**
 * Room database class for the app, providing access to the local tables and its associated DAO.
 * @Database annotation defines the entities (tables) associated with the database and the version number
 * (generic, nothing of significance).
 * @TypeConverters annotation specifies the type converters used in the database, to automatically convert timestamps
 * since they are not possible to be saved correctly in its raw form locally.
 *
 * @author 258030
 * @created 2024-11-13
 */
@Database(entities = [NewsItemLocal::class], version = 1, exportSchema = false)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


