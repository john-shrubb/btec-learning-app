package com.johnshrubb.stanwayeducation.otherComposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.StatusBarBG

/**
 * This app is a prototype and as such tutor logins are not implemented currently.
 * This composable simply displays that message.
 */

@Composable
fun TutorLoginScreen(
    rootNavController: NavController
) {
    Column {
        // As there is not enough content here to justify changing the entire colour scheme I've just hardcoded in the new colour into the elements. Sorry :-)
        StatusBarBG(color = Color(0xFF006098))
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text("This account type is not currently implemented in this app.")
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Button(
                onClick = {
                    rootNavController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006098),
                    contentColor = Color.White
                )
            ) {
                Text("Go back")
            }
        }

    }
}