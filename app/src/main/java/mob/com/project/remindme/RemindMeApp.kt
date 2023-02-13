package mob.com.project.remindme

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RemindMeApp:Application() {
    override fun onCreate() {
        super.onCreate()
        //initialize notification channel and manager
        val channelId = "reminderChannel"
        val channel = NotificationChannel(
            channelId,
            channelId,
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}