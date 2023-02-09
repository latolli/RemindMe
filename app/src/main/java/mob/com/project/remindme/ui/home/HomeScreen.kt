package mob.com.project.remindme.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mob.com.project.remindme.R
import mob.com.project.remindme.navigation.BottomNavBar
import mob.com.project.remindme.ui.theme.*
import mob.com.project.remindme.viewmodel.ListViewModel
import java.time.LocalDateTime

private enum class ModifyPopupState {
    Active, Closed, Modify
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: ListViewModel,
    navHostController: NavHostController
) {
    val modifyPopupState = rememberSaveable {
        mutableStateOf(ModifyPopupState.Closed)
    }
    //states for keeping track of current values for selected reminder
    val msgState = rememberSaveable { mutableStateOf("") }
    val locationXState: MutableState<Float> = rememberSaveable { mutableStateOf(0.0f) }
    val locationYState: MutableState<Float> = rememberSaveable { mutableStateOf(0.0f) }
    val reminderIdState: MutableState<Long?> = rememberSaveable { mutableStateOf(null) }
    val reminderTime = rememberSaveable { mutableStateOf("") }
    val creationTime = rememberSaveable { mutableStateOf("") }
    val creatorIdState: MutableState<Long?> = rememberSaveable { mutableStateOf(null) }
    val reminderSeenState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    val selectedReminder = homeViewModel.findReminder(reminderIdState.value).collectAsState(initial = null)
    //val dateTimeState: MutableState<LocalDateTime> = rememberSaveable{mutableStateOf(LocalDateTime.now())}

    val itemListState = homeViewModel.reminderListF.collectAsState(initial = listOf())

    Scaffold(
        bottomBar = { BottomNavBar(navController = navHostController)},
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                modifyPopupState.value = ModifyPopupState.Active
            }) {
                Text("+")
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
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {
                            //update states
                            msgState.value = item.message
                            locationXState.value = item.location_x
                            locationYState.value = item.location_y
                            reminderIdState.value = item.reminderId
                            reminderTime.value = item.reminder_time
                            creationTime.value = item.creation_time
                            creatorIdState.value = item.creator_id
                            reminderSeenState.value = item.reminder_seen
                            //if existing item is clicked, set pop up state to modify
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
                                    text = "Message: ",
                                    color = PurpleDefault,
                                )
                                Text(
                                    text = "${item.message}",
                                    color = Color.Black
                                )
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.calendarpurple),
                                    contentDescription = "Calendar image",
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                                Text(
                                    text = " ${LocalDateTime.parse(item.reminder_time).year} ${LocalDateTime.parse(item.reminder_time).month} ${LocalDateTime.parse(item.reminder_time).dayOfMonth}    ",
                                    color = Color.Black
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.clockpurple),
                                    contentDescription = "Clock image",
                                    modifier = Modifier
                                        .size(24.dp)
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
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.pin_tp),
                                    contentDescription = "Map pin image",
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                                Text(
                                    text = "  ${item.location_x} , ${item.location_y}",
                                    color = Color.Black
                                )
                            }
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth(0.90f)
                            .height(1.5.dp)
                            .background(PurpleDefault)
                            .align(alignment = Alignment.Center)
                        )
                    }
                }
            }
            //check if modify popup is active or closed
            when(modifyPopupState.value) {
                //if active which means we are adding new reminder
                ModifyPopupState.Active -> {
                    ModifyReminder(
                        //creation_time = now
                        creation_time = LocalDateTime.now().toString(),
                        //on save click close popup and add new reminder
                        onClickSave = {
                            homeViewModel.addReminder(
                                reminder = it)
                            modifyPopupState.value = ModifyPopupState.Closed},
                        //on dismiss or delete click close popup
                        onClickDismiss = {modifyPopupState.value = ModifyPopupState.Closed},
                        onClickDelete = {modifyPopupState.value = ModifyPopupState.Closed}
                    )
                }
                //if closed
                ModifyPopupState.Closed -> { /* do nothing */}
                //if on modify state
                ModifyPopupState.Modify -> {
                    ModifyReminder(
                        //on save click close popup and update
                        //pass current values
                        id = reminderIdState.value,
                        message = msgState.value,
                        location_x = locationXState.value.toString(),
                        location_y = locationYState.value.toString(),
                        reminder_time = reminderTime.value,
                        creation_time = creationTime.value,
                        creator_id = creatorIdState.value,
                        reminder_seen = reminderSeenState.value,
                        onClickSave = {
                            homeViewModel.updReminder(
                                reminder = it)
                            modifyPopupState.value = ModifyPopupState.Closed},
                        //on dismiss click close popup
                        onClickDismiss = {modifyPopupState.value = ModifyPopupState.Closed},
                        //on delete click delete item
                        onClickDelete = {
                            selectedReminder.value?.let { it1 -> homeViewModel.delReminder(reminder = it1) }
                            modifyPopupState.value = ModifyPopupState.Closed}
                    )
                }
            }
        }
    }
}
