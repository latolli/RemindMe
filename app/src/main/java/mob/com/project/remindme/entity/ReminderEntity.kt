package mob.com.project.remindme.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) var reminderId: Long? = null,
    val message: String,
    val location_x: Float,
    val location_y: Float,
    val reminder_time: String,
    val creation_time: String,
    val creator_id: Long? = null,
    val reminder_seen: Boolean
)
//data class ReminderEntity(
//    @PrimaryKey(autoGenerate = true) var reminderId: Long? = null,
//    val title: String,
//    val time: String
//)