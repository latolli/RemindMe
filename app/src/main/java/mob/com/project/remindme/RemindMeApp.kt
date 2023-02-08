package mob.com.project.remindme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RemindMeApp:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}