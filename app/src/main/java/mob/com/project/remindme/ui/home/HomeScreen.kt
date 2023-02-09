package mob.com.project.remindme.ui.home

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
    val locationxState: MutableState<Float> = rememberSaveable { mutableStateOf(0.0f) }
    val locationyState: MutableState<Float> = rememberSaveable { mutableStateOf(0.0f) }
    val reminderIdState: MutableState<Long?> = rememberSaveable { mutableStateOf(null) }
    val reminderTime = rememberSaveable { mutableStateOf("") }
    val creationTime = rememberSaveable { mutableStateOf("") }
    val creatorIdState: MutableState<Long?> = rememberSaveable { mutableStateOf(null) }
    val reminderSeenState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    val selectedReminder = homeViewModel.findReminder(reminderIdState.value).collectAsState(initial = null)

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
                            locationxState.value = item.location_x
                            locationyState.value = item.location_y
                            reminderIdState.value = item.reminderId
                            reminderTime.value = item.reminder_time
                            creationTime.value = item.creation_time
                            creatorIdState.value = item.creator_id
                            reminderSeenState.value = item.reminder_seen
                            //if existing item is clicked, set pop up state to modify
                            modifyPopupState.value = ModifyPopupState.Modify
                        }
                        .height(100.dp)){
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(10.dp),
                            text = "${item.message} --- ${item.reminder_time}",
                            color = Color.Black)
                        Spacer(modifier = Modifier
                            .fillMaxWidth(0.90f)
                            .height(1.5.dp)
                            .background(PurpleDefault)
                            .align(Alignment.BottomCenter)
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
                        location_x = locationxState.value.toString(),
                        location_y = locationyState.value.toString(),
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
