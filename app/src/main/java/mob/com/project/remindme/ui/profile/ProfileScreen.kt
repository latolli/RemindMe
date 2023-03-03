package mob.com.project.remindme.ui.profile

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.work.WorkManager
import mob.com.project.remindme.R
import mob.com.project.remindme.navigation.BottomNavBar
import mob.com.project.remindme.ui.theme.PurpleDefault
import mob.com.project.remindme.ui.theme.RemindMeTheme
import mob.com.project.remindme.viewmodel.ListViewModel

@Composable
fun ProfileScreen(
    homeViewModel: ListViewModel,
    navHostController: NavHostController,
    context: Context,
    modifier: Modifier
) {
    RemindMeTheme(darkTheme = true) {
        Scaffold(
            bottomBar = { BottomNavBar(navController = navHostController) },
            content = { ProfileScreenContent(homeViewModel, context, modifier) }
        )
    }
}

@Composable
fun ProfileScreenContent(homeViewModel: ListViewModel, context: Context, modifier: Modifier) {
    val username = stringResource(id = R.string.username)
    //initialize current reminder items and work manager
    val itemListState = homeViewModel.reminderListF.collectAsState(initial = listOf())
    val workManager = WorkManager.getInstance(context)

    Column (modifier = modifier
        .padding(20.dp)
        .fillMaxWidth()
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(100.dp))

        Image(painter = painterResource(id = R.drawable.userpurple),
            contentDescription = "Login image",
            modifier = Modifier
                .fillMaxWidth()
                .size(64.dp)
        )

        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Username:   $username",
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, PurpleDefault), shape = RoundedCornerShape(corner = CornerSize(50.dp)))
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Email:          example@gmail.com",
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, PurpleDefault), shape = RoundedCornerShape(corner = CornerSize(50.dp)))
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            //delete all reminders and work requests
            workManager.cancelAllWork()
            itemListState.value.forEach {
                homeViewModel.delReminder(it)
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))) {
            Text(text = "Delete all reminders")
        }
    }
}