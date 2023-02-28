package mob.com.project.remindme.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import mob.com.project.remindme.notification.showNotification
import mob.com.project.remindme.utils.calculateTimeBetween
import java.time.LocalDateTime


class ReminderWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val message = inputData.getString("message")
        val notificationId = inputData.getInt("notificationId", 0)
        val reminderTime = inputData.getString("reminderTime")
        var distance = 200

        //every 10 seconds check if we have reached reminder time or are in correct location
        if (reminderTime != "") {
            while (calculateTimeBetween(LocalDateTime.now(), LocalDateTime.parse(reminderTime)) > 5 && distance > 2) {
                delay(10000)
            }
        }
        else {
            while (distance > 2) {
                delay(10000)
            }
        }

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
