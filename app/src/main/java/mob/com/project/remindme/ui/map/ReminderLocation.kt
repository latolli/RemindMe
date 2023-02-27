package mob.com.project.remindme.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import mob.com.project.remindme.ui.theme.PurpleDefault
import mob.com.project.remindme.utils.rememberMapViewWithLifecycle

@Composable
fun ReminderLocation(
    lat: Float = 0.0f,
    lng: Float = 0.0f,
    onClickSaveLat: (Float) -> Unit,
    onClickSaveLng: (Float) -> Unit,
    onClickDismiss: () -> Unit
) {
    val mapView: MapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val latState = remember{mutableStateOf(lat)}
    val lngState = remember{mutableStateOf(lng)}

    val startState = remember{mutableStateOf(true)}

    Dialog(onDismissRequest = onClickDismiss) {
        Row(
            modifier = Modifier
                .fillMaxSize(0.95f)
                .clip(RoundedCornerShape(10.dp))
        ) {
            AndroidView({ mapView }) {
                coroutineScope.launch {
                    val map = mapView.awaitMap()
                    map.uiSettings.isZoomControlsEnabled = true
                    map.uiSettings.isScrollGesturesEnabled = true
                    //if map was just opened, move camera to oulu
                    if (startState.value) {
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(65.06, 25.47),
                                10f
                            )
                        )
                        startState.value = false
                    }

                    map.setOnMapClickListener { position ->
                        latState.value = position.latitude.toFloat()
                        lngState.value = position.longitude.toFloat()
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //on cancel click, call clickDismiss
            androidx.compose.material.Button(onClick = { onClickDismiss() }) {
                Text(text = "Cancel",
                    color = Color.White)
            }
            //on confirm click, call lat and lng saves
            androidx.compose.material.Button(onClick = {
                onClickSaveLat(latState.value)
                onClickSaveLng(lngState.value)
            }) {
                Text(text = "Confirm",
                    color = Color.White)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 70.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Lat: ${latState.value}, Lng: ${lngState.value}",
                color = PurpleDefault,
                style = TextStyle(fontSize = 22.sp, background = Color.White)
            )
        }
    }
}
