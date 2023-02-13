package mob.com.project.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mob.com.project.remindme.ui.home.HomeScreen
import mob.com.project.remindme.ui.login.LoginScreen
import mob.com.project.remindme.ui.profile.ProfileScreen
import mob.com.project.remindme.ui.theme.RemindMeTheme
import mob.com.project.remindme.ui.theme.WhiteSurface
import mob.com.project.remindme.viewmodel.ListViewModel

sealed class Destination(val route: String) {
    object Home: Destination("home")
    object Login: Destination("login")
    object Profile: Destination("profile")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listViewModel: ListViewModel by viewModels()
        setContent {
            RemindMeTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WhiteSurface
                ) {
                    //create navController
                    val navController = rememberNavController()
                    NavigationAppHost(navController = navController, viewModel = listViewModel)
                }
            }
        }
    }
}
//navigation host
@Composable
fun NavigationAppHost(navController: NavHostController, viewModel: ListViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable(Destination.Login.route) {
            LoginScreen(modifier = Modifier.fillMaxSize(), navHostController = navController)
        }
        composable(Destination.Home.route) {
            HomeScreen(homeViewModel = viewModel, navHostController = navController)
        }
        composable(Destination.Profile.route) {
            ProfileScreen(navHostController = navController, modifier = Modifier)
        }
    }
}