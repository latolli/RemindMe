package mob.com.project.remindme.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultClient(private val context: Context, private val client: FusedLocationProviderClient): LocationClient {
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(timeInterval: Long): Flow<Location> {
        return callbackFlow {
            //check that we have permissions
            if(!context.hasLocationPermission()) {
                throw LocationClient.LocationException("No permission for location tracking")
            }
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //check if gps & network are enabled
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!gpsEnabled && !networkEnabled) {
                throw LocationClient.LocationException("No gps / network enabled")
            }

            //make location request
            val locationRequest = LocationRequest.create()
                .setInterval(timeInterval)
                .setFastestInterval(timeInterval)

            //make location callback
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationList: LocationResult) {
                    super.onLocationResult(locationList)
                    //get newest = last location from the list
                    locationList.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}