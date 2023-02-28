package mob.com.project.remindme.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mob.com.project.remindme.R
import mob.com.project.remindme.notification.showNotification


class LocationService: Service() {

    //initialize coroutine scope and location client
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    //on create init location client to be default client
    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultClient(applicationContext, LocationServices.getFusedLocationProviderClient(applicationContext))
    }

    //values for starting / stopping location tracking
    companion object {
        const val START_TRACK = "START_TRACK"
        const val STOP_TRACK = "STOP_TRACK"
    }

    //override on start command function
    override fun onStartCommand(intent: Intent?, flags: Int, Id: Int): Int {
        when(intent?.action) {
            START_TRACK -> startTrack()
            STOP_TRACK -> stopTrack()
        }
        return super.onStartCommand(intent, flags, Id)
    }

    //function for starting location tracking
    private fun startTrack() {
        //notification for informing user about location tracking
        val notification = NotificationCompat.Builder(this, "reminderChannel")
            .setContentTitle("Location tracking enabled")
            .setContentText("Location: -")
            .setSmallIcon(R.drawable.pin_tp)
            .setOngoing(true)
            .setSilent(true)

        //init notification manager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //get new location on set time interval and update notification shown to user
        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val updatedNotification = notification.setContentText(
                    "Location: (${location.latitude.toString().take(5)}, ${location.longitude.toString().take(5)})"
                )
                //show updated notification
                notificationManager.notify(9999, updatedNotification.build())
            }
            .launchIn(coroutineScope)
        startForeground(9999, notification.build())
    }

    //function for stopping location tracking
    private fun stopTrack() {
        stopForeground(true)
        stopSelf()
    }

    //function for destroying current service
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}