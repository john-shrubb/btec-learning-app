package com.johnshrubb.stanwayeducation.otherComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Allow the user to pick their account type.
 */
@Composable
fun ChooseLoginScreen(
    rootNavController : NavController
) {
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        // The "I'm a student" login option
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F) // Fill up as much screen as it can. Should amount to about half.
                .background(MaterialTheme.colorScheme.primary) // Since this is a prototype I can afford just using the material theme's primary for the theming.
                .clickable(onClick = { // Entire block is clickable.
                    rootNavController.navigate("studentLogin") {
                        popUpTo("studentLogin")
                    }
                }),
            // Center text
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("I'm a student", fontSize = 40.sp, color = Color.White)
        }
        // The "I'm a tutor" login option
        // Pretty much the same as the above, theoretically I could simplify this into a composable function
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .background(Color(0xFF006098)) // Cool looking blue. Were this a functioning app there would be two themes which would swap when the user taps on either.
                .clickable(onClick = {
                    rootNavController.navigate("tutorLogin") {
                        popUpTo("tutorLogin")
                    }
                }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("I'm a tutor", fontSize = 40.sp, color = Color.White)
        }
    }
}