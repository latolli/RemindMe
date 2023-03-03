package mob.com.project.remindme.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import mob.com.project.remindme.R
import mob.com.project.remindme.entity.ReminderEntity
import mob.com.project.remindme.ui.map.ReminderLocation
import mob.com.project.remindme.ui.theme.WhiteSurface

private enum class ModifyMapState {
    Active, Closed
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyReminder(
    id: Long? = null,
    message: String = "",
    location_x: Float = 0.0f,
    location_y: Float = 0.0f,
    reminder_time: String = "",
    creation_time: String = "",
    creator_id: Long = 0,
    reminder_seen: Boolean = false,
    notificationId: Int = 0,
    isNew: Boolean = false,
    onClickSave: (ReminderEntity) -> Unit,
    onClickDismiss: () -> Unit,
    onClickDelete: () -> Unit
) {
    //state for keeping track of state of reminder edit popup
    val modifyMapState = rememberSaveable {
        mutableStateOf(ModifyMapState.Closed)
    }

    //reminder data states
    val messageState = rememberSaveable { mutableStateOf(message) }
    val latState = rememberSaveable { mutableStateOf(location_x) }
    val lngState = rememberSaveable { mutableStateOf(location_y) }
    val reminderTimeState = rememberSaveable { mutableStateOf(reminder_time) }
    val reminderSeenState = rememberSaveable { mutableStateOf(reminder_seen) }

    //states for calendar and clock picker
    val calendarState = rememberSheetState()
    val clockState = rememberSheetState()
    val calendarStringState = rememberSaveable { mutableStateOf("") }
    val clockStringState = rememberSaveable { mutableStateOf("") }

    //dialogs
    CalendarDialog(state = calendarState,
        selection = CalendarSelection.Date { date ->
            //if new date was selected, update state
            calendarStringState.value = "$date"
        })
    ClockDialog(state = clockState,
        config = ClockConfig(
            is24HourFormat = true,
        ),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            //if new time was selected, update state
            //if hours or minutes is smaller than 10, add 0 before the number
            if (hours <= 9 && minutes <= 9) {
                clockStringState.value = "0$hours:0$minutes:00.0"
            } else if (hours <= 9) {
                clockStringState.value = "0$hours:$minutes:00.0"
            } else if (minutes <= 9) {
                clockStringState.value = "$hours:0$minutes:00.0"
            } else {
                clockStringState.value = "$hours:$minutes:00.0"
            }
        }
    )

    Dialog(onDismissRequest = onClickDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = WhiteSurface)
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            //top row that displays message and has delete button
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //if we are adding new reminder, don't show id and delete buttons
                if (!isNew) {
                    Text(text = "Reminder ID: $id", color = Color.Black)
                    Image(painter = painterResource(id = R.drawable.deletepurple),
                        contentDescription = "Delete image",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onClickDelete() }
                    )
                } else {
                    Text(text = "New reminder", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            //middle row that displays all the reminder data
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    //field for modifying message
                    OutlinedTextField(
                        value = messageState.value,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text -> messageState.value = text },
                        label = { androidx.compose.material.Text(text = "Message") },
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    //date and time pickers
                    Text(text = "Pick reminder time:", color = Color.Black)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(painter = painterResource(id = R.drawable.calendarpurple),
                            contentDescription = "Calendar image",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { calendarState.show() }
                        )
                        Image(painter = painterResource(id = R.drawable.clockpurple),
                            contentDescription = "Calendar image",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { clockState.show() }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    //button for picking location
                    androidx.compose.material.Button(
                        onClick = {//navHostController.navigate(Destination.Map.route)
                            modifyMapState.value = ModifyMapState.Active
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    ) {
                        androidx.compose.material.Text(text = "Pick location")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Lat: ${
                            latState.value.toString().take(6)
                        }, Lng: ${lngState.value.toString().take(6)}", color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            //bottom row for accept / cancel buttons
            Row(
                Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(painter = painterResource(id = R.drawable.cancelpurple_tp),
                    contentDescription = "Cancel image",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClickDismiss() }
                )
                Image(painter = painterResource(id = R.drawable.checkpurple),
                    contentDescription = "Save image",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            //if new time was picked, replace reminder time and set reminder seen to false
                            if (calendarStringState.value != "" && clockStringState.value != "") {
                                reminderTimeState.value =
                                    "${calendarStringState.value}T${clockStringState.value}"
                                reminderSeenState.value = false
                            }
                            //if new location was picked, set reminder seen to false
                            if (latState.value != 0.0f && lngState.value != 0.0f) {
                                reminderSeenState.value = false
                            }

                            onClickSave(
                                ReminderEntity(
                                    reminderId = id,
                                    message = messageState.value,
                                    location_x = latState.value,
                                    location_y = lngState.value,
                                    reminder_time = reminderTimeState.value,
                                    creation_time = creation_time,
                                    creator_id = creator_id,
                                    reminder_seen = reminderSeenState.value,
                                    notificationId = notificationId
                                )
                            )
                        }
                )
            }
        }
    }
    when (modifyMapState.value) {
        //if map is active
        ModifyMapState.Active -> {
            ReminderLocation(
                lat = latState.value,
                lng = lngState.value,
                //on save click get new lat and lng values and close popup
                onClickSaveLat = {
                    latState.value = it
                },
                onClickSaveLng = {
                    lngState.value = it
                    modifyMapState.value = ModifyMapState.Closed
                },

                //on cancel click close map popup
                onClickDismiss = {
                    modifyMapState.value = ModifyMapState.Closed
                }
            )
        }
        //if map is closed
        ModifyMapState.Closed -> {/* do nothing */
        }
    }
}
