package mob.com.project.remindme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mob.com.project.remindme.entity.ReminderEntity

@Database(entities = [ReminderEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppInstance(context: Context): AppDatabase {
            //if there is no instance atm, create new
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "reminderDB.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}