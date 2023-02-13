package mob.com.project.remindme.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mob.com.project.remindme.R

//function for showing notification
fun showNotification(
    context: Context,
    channelId: String,
    notificationId: Int,
    title: String,
    content: String,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.notification)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(priority)

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}