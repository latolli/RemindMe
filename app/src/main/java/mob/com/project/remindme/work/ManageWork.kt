package mob.com.project.remindme.work

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

//function for creating new work request
fun setReminderRequest(context: Context, message: String, notificationId: Int, reminderTime: String) {
    val workManager = WorkManager.getInstance(context)

    //initialize input data
    val data = Data.Builder()
        .putInt("notificationId", notificationId)
        .putString("message", message)
        .putString("reminderTime", reminderTime)
        .build()

    //create request with input data and unique tag
    val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
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
fun replaceReminderRequest(context: Context, message: String, notificationId: Int, reminderTime: String) {
    //cancel old requests
    cancelReminderRequest(context, notificationId)
    //create new request with new values
    setReminderRequest(context, message, notificationId, reminderTime)
}