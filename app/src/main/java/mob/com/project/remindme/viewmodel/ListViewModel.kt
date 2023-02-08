package mob.com.project.remindme.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import mob.com.project.remindme.database.repository.ReminderRepository
import mob.com.project.remindme.entity.ReminderEntity
import javax.inject.Inject

interface HomeViewModelAbstract {
    val reminderListF: Flow<List<ReminderEntity>>
    fun findReminder(reminderId: Long?): Flow<ReminderEntity>
    fun addReminder(reminder: ReminderEntity)
    fun delReminder(reminder: ReminderEntity)
    fun updReminder(reminder: ReminderEntity)
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel(), HomeViewModelAbstract {

    override val reminderListF: Flow<List<ReminderEntity>> = reminderRepository.getAll()
    override fun findReminder(reminderId: Long?): Flow<ReminderEntity> = reminderRepository.findOne(reminderId = reminderId)

    //initialize coroutine scope for add, del and update methods
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun addReminder(reminder: ReminderEntity) {
        coroutineScope.launch {
            reminderRepository.insert(reminder = reminder)
        }
    }

    override fun delReminder(reminder: ReminderEntity) {
        coroutineScope.launch {
            reminderRepository.delete(reminder = reminder)
        }
    }

    override fun updReminder(reminder: ReminderEntity) {
        coroutineScope.launch {
            reminderRepository.update(reminder = reminder)
        }
    }
}