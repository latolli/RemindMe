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
import mob.com.project.remindme.entity.ReminderEntity
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
        val reminderTitleState = rememberSaveable { mutableStateOf("") }
        val reminderIdState: MutableState<Long?> = rememberSaveable { mutableStateOf(null) }
        val selectedReminder = homeViewModel.findReminder(reminderIdState.value).collectAsState(initial = null)

        Scaffold(
            bottomBar = { BottomNavBar(navController = navHostController)},
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    modifyPopupState.value = ModifyPopupState.Active
                }) {
                    Text("+")
                }
            },
        ) {
            val itemListState = homeViewModel.reminderListF.collectAsState(initial = listOf())

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = WhiteSurface
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(WhiteSurface)
                ) {
                    items(itemListState.value.size){ i ->
                        val item = itemListState.value[i]
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .clickable {
                                reminderTitleState.value = item.title
                                reminderIdState.value = item.reminderId
                                //if existing item is clicked, set pop up state to modify
                                modifyPopupState.value = ModifyPopupState.Modify
                            }
                            .height(100.dp)){
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(10.dp),
                                text = "${item.title} --- ${item.time}",
                                color = Color.Black)
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(PurpleDefault)
                                .align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
                //check if modify popup is active or closed
                when(modifyPopupState.value) {
                    //if active
                    ModifyPopupState.Active -> {
                        ModifyReminder(
                            //on save click close popup and add new reminder
                            onClickSave = {
                                homeViewModel.addReminder(
                                    reminder = ReminderEntity(
                                        title = it,
                                        time = LocalDateTime.now().toString()
                                    ))
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
                            title = reminderTitleState.value,
                            onClickSave = {
                                homeViewModel.updReminder(
                                    reminder = ReminderEntity(
                                        reminderId = reminderIdState.value,
                                        title = it,
                                        time = LocalDateTime.now().toString()
                                    ))
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
