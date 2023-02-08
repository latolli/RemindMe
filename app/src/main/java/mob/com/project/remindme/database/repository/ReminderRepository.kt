package mob.com.project.remindme.database.repository

import kotlinx.coroutines.flow.Flow
import mob.com.project.remindme.database.ReminderDao
import mob.com.project.remindme.entity.ReminderEntity

class ReminderRepository (
    private val reminderDao: ReminderDao
        ){
    //methods
    fun getAll(): Flow<List<ReminderEntity>> = reminderDao.getAll()

    fun findOne(reminderId: Long?): Flow<ReminderEntity> = reminderDao.findOne(reminderId = reminderId)

    suspend fun insert(reminder: ReminderEntity) = reminderDao.insert(reminder = reminder)

    suspend fun update(reminder: ReminderEntity) = reminderDao.update(reminder = reminder)

    suspend fun delete(reminder: ReminderEntity) = reminderDao.delete(reminder = reminder)
}