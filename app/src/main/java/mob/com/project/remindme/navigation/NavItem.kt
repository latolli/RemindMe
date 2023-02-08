package mob.com.project.remindme.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import mob.com.project.remindme.Destination
import mob.com.project.remindme.R

sealed class NavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
){
    object Home: NavItem(R.string.home, R.drawable.hut, Destination.Home.route)
    object Profile: NavItem(R.string.profile, R.drawable.user, Destination.Profile.route)
    object Logout: NavItem(R.string.logout, R.drawable.logout, Destination.Login.route)
}
