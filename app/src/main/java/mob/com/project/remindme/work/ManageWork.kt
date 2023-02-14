package mob.com.project.remindme.work

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import mob.com.project.remindme.entity.ReminderEntity
import java.util.concurrent.TimeUnit

//function for creating new work request
fun setReminderRequest(context: Context, minutes: Long, message: String, notificationId: Int) {
    val workManager = WorkManager.getInstance(context)

    //initialize input data
    val data = Data.Builder()
        //.putLong("reminderId", notificationId.toLong())
        .putInt("notificationId", notificationId)
        .putString("message", message)
        //.putFloat("location_x", reminderEntity.location_x)
        //.putFloat("location_y", reminderEntity.location_y)
        //.putString("reminder_time", reminderEntity.reminder_time)
        //.putString("creation_time", reminderEntity.creation_time)
        //.putLong("creator_id", reminderEntity.creator_id)
        //.putBoolean("reminder_seen", reminderEntity.reminder_seen)
        .build()

    //create request with initial delay and input data and unique tag
    val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(minutes, TimeUnit.MINUTES)
        .setInputData(data)
        .addTag("$notificationId")
        .build()

    //enqueue request
    workManager.enqueue(reminderRequest)
}

//function for cancelling existing work request
fun cancelReminderRequest(context: Context, tag: Int) {
    val workManager = WorkManager.getInstance(context)
    //cancel all work with this tag
    workManager.cancelAllWorkByTag("$tag")
}

//function for replacing existing work request
fun replaceReminderRequest(context: Context, minutes: Long, message: String, notificationId: Int) {
    //cancel old requests
    cancelReminderRequest(context, notificationId)
    //create new request with new values
    setReminderRequest(context, minutes, message, notificationId)
}