package mob.com.project.remindme.work

import android.content.Context
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
                content = "Notification ID: $notificationId"
            )
        }
        else {
            showNotification(
                context = context,
                channelId = "reminderChannel",
                notificationId = notificationId,
                title = "Reminder $notificationId",
                content = "Notification ID: $notificationId"
            )
        }

        return Result.success()
    }
}
