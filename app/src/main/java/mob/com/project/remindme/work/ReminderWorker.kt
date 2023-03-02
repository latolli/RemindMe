package mob.com.project.remindme.work

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import mob.com.project.remindme.notification.showNotification
import mob.com.project.remindme.utils.calculateTimeBetween
import mob.com.project.remindme.utils.euclideanDistance
import java.time.LocalDateTime


class ReminderWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val message = inputData.getString("message")
        val notificationId = inputData.getInt("notificationId", 0)
        val reminderTime = inputData.getString("reminderTime")
        val reminderLat = inputData.getFloat("lat", 0.0f)
        val reminderLng = inputData.getFloat("lng", 0.0f)
        //variables for keeping track of user's location
        var userLat = 0.0f
        var userLng = 0.0f

        //initialize location client and get initial user location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    userLat = location.latitude.toFloat()
                    userLng = location.longitude.toFloat()
                }
            }
        //every 10 seconds check if we have reached reminder time or are in correct location
        //if we have both location and time
        if (reminderTime != "" && reminderLat != 0.0f && reminderLng != 0.0f) {
            while (calculateTimeBetween(LocalDateTime.now(), LocalDateTime.parse(reminderTime)) > 5 && euclideanDistance(reminderLat, reminderLng, 65.06f, 25.47f) > 0.0005f) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            //Log.d("LOCATION", "${location.latitude}, ${location.longitude}")
                            //Log.d("DISTANCE", "${euclideanDistance(reminderLat, reminderLng, userLat, userLng)}")
                            //Log.d("INFO", "$notificationId, $reminderTime, $message")
                            userLat = location.latitude.toFloat()
                            userLng = location.longitude.toFloat()
                        }
                    }
                delay(10000)
            }
        }
        //if we have only time
        else if (reminderTime != "") {
            while (calculateTimeBetween(LocalDateTime.now(), LocalDateTime.parse(reminderTime)) > 5) {
                delay(10000)
            }
        }
        //if we have only location
        else {
            while (euclideanDistance(reminderLat, reminderLng, 65.06f, 25.47f) > 0.0005f) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            //Log.d("LOCATION", "${location.latitude}, ${location.longitude}")
                            //Log.d("DISTANCE", "${euclideanDistance(reminderLat, reminderLng, userLat, userLng)}")
                            //Log.d("INFO", "$notificationId, $reminderTime, $message")
                            userLat = location.latitude.toFloat()
                            userLng = location.longitude.toFloat()
                        }
                    }
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
