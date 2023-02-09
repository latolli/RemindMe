package mob.com.project.remindme.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import mob.com.project.remindme.R
import mob.com.project.remindme.entity.ReminderEntity
import mob.com.project.remindme.ui.theme.PurpleDefault
import mob.com.project.remindme.ui.theme.WhiteSurface
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyReminder(
    id: Long? = null,
    message: String = "",
    location_x: String = "0.0",
    location_y: String = "0.0",
    reminder_time: String = LocalDateTime.now().toString(),
    creation_time: String = "",
    creator_id: Long? = null,
    reminder_seen: Boolean = false,
    onClickSave: (ReminderEntity) -> Unit,
    onClickDismiss: () -> Unit,
    onClickDelete: () -> Unit,
) {
    //reminder data states
    val messageState = rememberSaveable { mutableStateOf(message) }
    val locationXState = rememberSaveable { mutableStateOf(location_x) }
    val locationYState = rememberSaveable { mutableStateOf(location_y) }
    val reminderTimeState = rememberSaveable { mutableStateOf(reminder_time) }

    //states for calendar and clock picker
    val calendarState = rememberSheetState()
    val clockState = rememberSheetState()
    val calendarStringState = rememberSaveable { mutableStateOf("") }
    val clockStringState = rememberSaveable { mutableStateOf("") }

    //dialogs
    CalendarDialog(state = calendarState,
        selection = CalendarSelection.Date {date ->
            //if new date was selected, update state
            calendarStringState.value = "$date"
    })
    ClockDialog(state = clockState,
        config = ClockConfig(
            is24HourFormat = true,
        ),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            //if new time was selected, update state
            //if hours of minutes is smaller than 10, add 0 before number
            if(hours < 9 && minutes < 9) {
                clockStringState.value = "0$hours:0$minutes:00.0"
            }
            else if (hours < 9) {
                clockStringState.value = "0$hours:$minutes:00.0"
            }
            else if (minutes < 9) {
                clockStringState.value = "$hours:0$minutes:00.0"
            }
            else {
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
            Row(Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "ID: ${id}", color = Color.Black)
                Image(painter = painterResource(id = R.drawable.deletepurple),
                    contentDescription = "Delete image",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClickDelete() }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            //middle row that displays all the reminder data
            Row(Modifier
                .fillMaxWidth()) {
                Column() {
                    //field for modifying message
                    OutlinedTextField(value = messageState.value,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text -> messageState.value = text},
                        label = { androidx.compose.material.Text(text = "Message") },
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    //date and time pickers
                    Text(text = "Pick reminder time:", color = Color.Black)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround) {
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


                    //Button(onClick = { calendarState.show() }) {
                    //    Text(text = "Pick date")
                    //}
                    //Button(onClick = { clockState.show() }) {
                    //    Text(text = "Pick time")
                    //}

                    Spacer(modifier = Modifier.height(20.dp))
                    //field for modifying location x
                    OutlinedTextField(value = locationXState.value,
                        onValueChange = { text -> locationXState.value = text},
                        label = { androidx.compose.material.Text(text = "Longitude") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    //field for modifying location y
                    OutlinedTextField(value = locationYState.value,
                        onValueChange = { text -> locationYState.value = text},
                        label = { androidx.compose.material.Text(text = "Latitude") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
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
                            //if new time was picked, replace reminder time
                            if (calendarStringState.value != "" && clockStringState.value != "") {
                                reminderTimeState.value = "${calendarStringState.value}T${clockStringState.value}"
                            }
                            //reminder_timeState.value = "${reminder_timeDate.value}T${reminder_timeClock.value}:00.0"
                            onClickSave(
                                ReminderEntity(
                                    reminderId = id,
                                    message = messageState.value,
                                    location_x = locationXState.value.toFloat(),
                                    location_y = locationYState.value.toFloat(),
                                    reminder_time = reminderTimeState.value,
                                    creation_time = creation_time,
                                    creator_id = creator_id,
                                    reminder_seen = reminder_seen
                                )
                            )
                        }
                )
            }
        }
    }
}