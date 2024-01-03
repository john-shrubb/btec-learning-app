package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuestionCorrect(
    quizNavController    : NavController,
    quizState            : QuizState,
) {
    /**
     * The quizContinue function handles the user clicking ok. It handles all the logic for where to go next.
      */
    fun quizContinue() {
        if (quizState.currentQuestion == 11) {
            quizNavController.navigate("quizFinishedScreen") {
                popUpTo(quizNavController.graph.id) {
                    inclusive = true
                }
            }
        } else {
            quizNavController.navigate("question?q=${quizState.currentQuestion}") {
                popUpTo(quizNavController.graph.id)
            }
        }
    }
    // Text that actually displays the "That's right" message
    Column(
        // Center the column in the center of the screen
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 200.dp),
    ) {
        Text(
            text = "That's right!",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center,
        )
        Button(onClick = {
            quizContinue()
        }) {
            Text("Continue")
        }
    }
}