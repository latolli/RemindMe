package mob.com.project.remindme.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mob.com.project.remindme.R
import mob.com.project.remindme.entity.ReminderEntity
import mob.com.project.remindme.navigation.BottomNavBar
import mob.com.project.remindme.ui.theme.*
import mob.com.project.remindme.viewmodel.ListViewModel
import java.time.LocalDateTime
import androidx.compose.material.Checkbox
import com.google.android.gms.location.LocationServices
import mob.com.project.remindme.ui.map.ReminderLocation
import mob.com.project.remindme.utils.calculateTimeBetween
import mob.com.project.remindme.utils.euclideanDistance
import mob.com.project.remindme.work.cancelReminderRequest
import mob.com.project.remindme.work.replaceReminderRequest
import mob.com.project.remindme.work.setReminderRequest

private enum class ModifyPopupState {
    Active, Closed, Modify
}

private enum class ModifyLocationState {
    Active, Closed
}


@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: ListViewModel,
    navHostController: NavHostController,
    context: Context
) {
    //state for keeping track of state of reminder edit popup
    val modifyPopupState = rememberSaveable {
        mutableStateOf(ModifyPopupState.Closed)
    }
    //state for changing virtual location
    val modifyLocationState = rememberSaveable {
        mutableStateOf(ModifyLocationState.Closed)
    }

    //initialize variables for context and work manager
    var virtualUserLat = 0.0f
    var virtualUserLng = 0.0f
    var userLat = 0.0f
    var userLng = 0.0f
    //keep track of what next id should be
    var lastNotificationId = 0
    //boolean for checking if we should show all reminders or not and if we should track location
    val showAllState = remember{ mutableStateOf(false)}
    val showNearby = remember{ mutableStateOf(false)}
    val itemListState = homeViewModel.reminderListF.collectAsState(initial = listOf())

    //initialize variable for reminder entity to keep track of selected reminder
    var selectedReminder = ReminderEntity(reminderId = 0,
                                        message = "",
                                        location_x = 0.0f,
                                        location_y = 0.0f,
                                        reminder_time = "",
                                        creation_time = "",
                                        creator_id = 0,
                                        reminder_seen = false,
                                        notificationId = 0
        )

    //initialize location client and get initial user location
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location : Location? ->
            if (location != null) {
                virtualUserLat = location.latitude.toFloat()
                virtualUserLng = location.longitude.toFloat()
                userLat = location.latitude.toFloat()
                userLng = location.longitude.toFloat()
            }
        }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navHostController)},
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = PurpleDefault,
                contentColor = WhiteSurface,
                onClick = {
                modifyPopupState.value = ModifyPopupState.Active
            }) {
                Text("+", style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ))
            }
        }
    ) { padding -> 16.dp
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = WhiteSurface
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .background(WhiteSurface)
            ) {
                items(itemListState.value.size){ i ->
                    val item = itemListState.value[i]
                    lastNotificationId = maxOf(item.notificationId, lastNotificationId)
                    //on first iteration show top row
                    if (i == 0) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //button for enabling / disabling location tracking
                            androidx.compose.material.Button(
                                onClick = {
                                    modifyLocationState.value = ModifyLocationState.Active
                                },
                                modifier = Modifier
                                    .height(50.dp),
                                shape = RoundedCornerShape(corner = CornerSize(50.dp))
                            ) {
                                androidx.compose.material.Text(text = "Choose virtual location", style = TextStyle(fontSize = 18.sp))
                            }
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Show all",
                                color = PurpleDefault,
                                style = TextStyle(fontSize = 22.sp)
                            )
                            Checkbox(
                                checked = showAllState.value,
                                onCheckedChange = { showAllState.value = it }
                            )
                            Text(text = "Show nearby",
                                color = PurpleDefault,
                                style = TextStyle(fontSize = 22.sp)
                            )
                            Checkbox(
                                checked = showNearby.value,
                                onCheckedChange = { showNearby.value = it }
                            )
                        }
                    }
                    //if reminder is already occurred update reminderSeen value
                    //first check that reminder time is not empty
                    if (item.reminder_time != "") {
                        if (calculateTimeBetween(LocalDateTime.now(), LocalDateTime.parse(item.reminder_time)) <= 0) {
                            item.reminder_seen = true
                            homeViewModel.updReminder(reminder = item)
                        }
                    }
                    if (item.location_x != 0.0f && item.location_y != 0.0f) {
                        if (euclideanDistance(item.location_x, item.location_y, userLat, userLng) <= 0.0005f) {
                            item.reminder_seen = true
                            homeViewModel.updReminder(reminder = item)
                        }
                    }
                    //if reminder has no time / location constraints, update reminderSeen value
                    if (item.reminder_time == "" && item.location_x == 0.0f && item.location_y == 0.0f){
                        item.reminder_seen = true
                        homeViewModel.updReminder(reminder = item)
                    }
                    //display reminder if show all is selected or reminderSeen is true
                    if (showAllState.value || item.reminder_seen ||
                        (showNearby.value && euclideanDistance(item.location_x, item.location_y, virtualUserLat, virtualUserLng) < 0.05f && virtualUserLat != 0.0f && item.location_x != 0.0f)) {
                        //draw spacer
                        Box(modifier = Modifier
                            .fillMaxWidth()) {
                            Spacer(modifier = Modifier
                                .fillMaxWidth(0.90f)
                                .height(1.5.dp)
                                .background(PurpleDefault)
                                .align(alignment = Alignment.Center)
                            )
                        }
                        //display reminder
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .clickable {
                                //update data of selected reminder
                                selectedReminder = item
                                modifyPopupState.value = ModifyPopupState.Modify
                            }
                            .padding(horizontal = 12.dp, vertical = 20.dp)){
                            Column(){
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = "Message: ", color = PurpleDefault,
                                    )
                                    Text(
                                        text = "${item.message}", color = Color.Black,
                                    )
                                }
                                //if reminder time is set, display it
                                if (item.reminder_time != "") {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.calendarpurple),
                                            contentDescription = "Calendar image",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = " ${LocalDateTime.parse(item.reminder_time).year} ${LocalDateTime.parse(item.reminder_time).month} ${LocalDateTime.parse(item.reminder_time).dayOfMonth}    ",
                                            color = Color.Black
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.clockpurple),
                                            contentDescription = "Clock image",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        if (LocalDateTime.parse(item.reminder_time).minute > 9) {
                                            Text(
                                                text = "  ${LocalDateTime.parse(item.reminder_time).hour}:${LocalDateTime.parse(item.reminder_time).minute}",
                                                color = Color.Black
                                            )
                                        }
                                        else {
                                            Text(
                                                text = "  ${LocalDateTime.parse(item.reminder_time).hour}:0${LocalDateTime.parse(item.reminder_time).minute}",
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                                //if location is set, display it
                                if (item.location_x != 0.0f || item.location_y != 0.0f) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.pin_tp),
                                            contentDescription = "Map pin image",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "  ${item.location_x.toString().take(6)} , ${item.location_y.toString().take(6)}",
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //check if modify popup is active or closed
            when(modifyPopupState.value) {
                //if active (this means we are adding a new reminder)
                ModifyPopupState.Active -> {
                    ModifyReminder(
                        creation_time = LocalDateTime.now().toString(),
                        notificationId = lastNotificationId+1,
                        isNew = true,
                        //on save click close popup and add new reminder
                        onClickSave = {
                            homeViewModel.addReminder(reminder = it)
                            //make new work request, if we have some time or location information
                            if (it.reminder_time != "" || it.location_x != 0.0f || it.location_y != 0.0f) {
                                //create work request, notification id = current size of item list + 1
                                setReminderRequest(context, it.message, lastNotificationId+1, it.reminder_time, it.location_x, it.location_y)
                            }
                            //update lastNotificationId value
                            lastNotificationId = it.notificationId
                            //close popup
                            modifyPopupState.value = ModifyPopupState.Closed
                            },
                        //close popup on dismiss or delete click
                        onClickDismiss = {modifyPopupState.value = ModifyPopupState.Closed},
                        onClickDelete = {modifyPopupState.value = ModifyPopupState.Closed}
                    )
                }
                //if closed
                ModifyPopupState.Closed -> { /* do nothing */}
                //if on modify state
                ModifyPopupState.Modify -> {
                    ModifyReminder(
                        //pass current values
                        id = selectedReminder.reminderId,
                        message = selectedReminder.message,
                        location_x = selectedReminder.location_x,
                        location_y = selectedReminder.location_y,
                        reminder_time = selectedReminder.reminder_time,
                        creation_time = selectedReminder.creation_time,
                        creator_id = selectedReminder.creator_id,
                        reminder_seen = selectedReminder.reminder_seen,
                        notificationId = selectedReminder.notificationId,
                        //on save click close popup and update reminder
                        onClickSave = {
                            //update reminder in database
                            homeViewModel.updReminder(reminder = it) //asd
                            //replace current work request with new one, if we have some time or location information
                            if (it.reminder_time != "" || it.location_x != 0.0f || it.location_y != 0.0f) {
                                //replace old work request
                                replaceReminderRequest(context, it.message, it.notificationId, it.reminder_time, it.location_x, it.location_y)
                            }
                            modifyPopupState.value = ModifyPopupState.Closed},
                        //on dismiss click close popup
                        onClickDismiss = {modifyPopupState.value = ModifyPopupState.Closed},
                        //on delete click delete item
                        onClickDelete = {
                            //cancel work request made by this reminder
                            if (selectedReminder.reminder_time != "") {
                                cancelReminderRequest(context, selectedReminder.notificationId)
                            }
                            //delete reminder from database
                            homeViewModel.delReminder(reminder = selectedReminder)
                            modifyPopupState.value = ModifyPopupState.Closed
                            }
                    )
                }
            }
            //check state of modifying virtual location
            when(modifyLocationState.value) {
                ModifyLocationState.Active -> {
                    ReminderLocation(
                        lat = virtualUserLat,
                        lng = virtualUserLng,
                        //on save click get new lat and lng values and close popup
                        onClickSaveLat = {
                            virtualUserLat = it
                        },
                        onClickSaveLng = {
                            virtualUserLng = it
                            modifyLocationState.value = ModifyLocationState.Closed
                        },

                        //on cancel click close map popup
                        onClickDismiss = {
                            modifyLocationState.value = ModifyLocationState.Closed
                        }
                    )
                }
                ModifyLocationState.Closed -> {/* do nothing */ }
            }
        }
    }
}
