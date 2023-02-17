package mob.com.project.remindme.work

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

//function for creating new work request
fun setReminderRequest(context: Context, delay: Long, message: String, notificationId: Int) {
    val workManager = WorkManager.getInstance(context)

    //initialize input data
    val data = Data.Builder()
        .putInt("notificationId", notificationId)
        .putString("message", message)
        .build()

    //create request with initial delay and input data and unique tag
    val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delay, TimeUnit.SECONDS)
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
fun replaceReminderRequest(context: Context, delay: Long, message: String, notificationId: Int) {
    //cancel old requests
    cancelReminderRequest(context, notificationId)
    //create new request with new values
    setReminderRequest(context, delay, message, notificationId)
}