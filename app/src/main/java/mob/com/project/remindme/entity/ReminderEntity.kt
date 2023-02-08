package mob.com.project.remindme.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) var reminderId: Long? = null,
    val title: String,
    val time: String
)