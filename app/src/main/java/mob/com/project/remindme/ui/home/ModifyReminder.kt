package mob.com.project.remindme.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import mob.com.project.remindme.R
import mob.com.project.remindme.ui.theme.PurpleDefault
import mob.com.project.remindme.ui.theme.WhiteSurface

@Composable
fun ModifyReminder(
    title: String = "",
    onClickSave: (String) -> Unit,
    onClickDismiss: () -> Unit,
    onClickDelete: () -> Unit,
) {
    val titleState = rememberSaveable {
        mutableStateOf(title)
    }
    val textState = rememberSaveable {
        mutableStateOf(title)
    }
    Dialog(onDismissRequest = onClickDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = WhiteSurface)
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            //top row that displays title and has delete button
            Row(Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                BasicTextField(
                    value = titleState.value,
                    onValueChange = { input ->
                        titleState.value = input
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp))
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
                    OutlinedTextField(value = textState.value,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text -> textState.value = text},
                        label = { androidx.compose.material.Text(text = "Reminder description") },
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(value = textState.value,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text -> textState.value = text},
                        label = { androidx.compose.material.Text(text = "Reminder description") },
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(value = textState.value,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text -> textState.value = text},
                        label = { androidx.compose.material.Text(text = "Reminder description") },
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
                        .clickable { onClickSave(titleState.value) }
                )
            }
        }
    }
}