package mob.com.project.remindme.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    //fun for getting location updates
    fun getLocationUpdates(interval: Long): Flow<Location>
    //class for error messages
    class LocationException(message: String): Exception()
}