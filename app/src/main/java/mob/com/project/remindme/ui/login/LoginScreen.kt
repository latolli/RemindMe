package mob.com.project.remindme.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mob.com.project.remindme.Destination
import mob.com.project.remindme.R

@Composable
fun LoginScreen(
    modifier: Modifier,
    navHostController: NavHostController
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val correctUsername = stringResource(id = R.string.username)
    val correctPassword = stringResource(id = R.string.password)

    Column (modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Image(painter = painterResource(id = R.drawable.userpurple),
            contentDescription = "Login image",
            modifier = Modifier
                .fillMaxWidth()
                .size(64.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(value = username.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { text -> username.value = text},
            label = { Text(text = "Username") },
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = password.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {text -> password.value = text},
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            if (username.value == correctUsername && password.value == correctPassword) {
                navHostController.navigate(Destination.Home.route)
            }},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))) {
            Text(text = "Login")
        }
    }
}