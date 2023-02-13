package mob.com.project.remindme.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mob.com.project.remindme.notification.showNotification


class ReminderWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val message = inputData.getString("message")
        val notificationId = inputData.getInt("notificationId", 0)

        if (message != null) {
            showNotification(
                context = context,
                channelId = "reminderChannel",
                notificationId = notificationId,
                title = message,
                content = "Reminder $notificationId"
            )
        }
        else {
            showNotification(
                context = context,
                channelId = "reminderChannel",
                notificationId = notificationId,
                title = "Reminder $notificationId",
                content = "Reminder $notificationId"
            )
        }

        return Result.success()
    }
}
