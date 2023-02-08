package mob.com.project.remindme.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import mob.com.project.remindme.entity.ReminderEntity

//initialize data access object and methods for it
@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    fun getAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder WHERE reminderId=:reminderId")
    fun findOne(reminderId: Long?): Flow<ReminderEntity>

    @Insert
    suspend fun insert(reminder: ReminderEntity)

    @Delete
    suspend fun delete(reminder: ReminderEntity)

    @Update
    suspend fun update(reminder: ReminderEntity)
}