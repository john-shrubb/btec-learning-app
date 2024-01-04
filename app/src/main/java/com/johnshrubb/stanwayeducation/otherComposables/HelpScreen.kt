package com.johnshrubb.stanwayeducation.otherComposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.StatusBarBG

@Composable
fun HelpScreen(
    studentNavController : NavController
) {
    Column {
        StatusBarBG(color = MaterialTheme .colorScheme.primary)
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "What do the different difficulties do?",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text =
                "Quizzes are sorted into different difficulties which change how difficult the questions are." +
                        "\n\nEasy is the easiest difficulty, with an infinite time limit and second tries on every question." +
                        "\n\nMedium is an between difficulty, with a 20 second time limit, however you still get a second go on every question." +
                        "\n\nHard really kicks the difficulty up with no second goes and a 10 second time limit on each question.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                studentNavController.navigateUp()
            }) {
                Text(
                    text = "Go back"
                )
            }
        }
    }
}