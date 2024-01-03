package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun QuizFinished(
    homeNavController : NavController,
    quizState         : QuizState
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val quizScore by remember { mutableIntStateOf(quizState.currentScore) }
        // Congratulatory message.
        Text(
            text = "Congratulations! You finished the quiz!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        // Final score message.
        Text(
            text = "Your final score was:\n${quizScore}",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        // Go home button.
        Button(
            onClick = {
                homeNavController.navigate("studentHome") {
                    popUpTo(homeNavController.graph.id) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Continue")
        }
    }
}