package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.Quiz

/**
 * Confirm that the user actually wants to go ahead with the quiz.
 */
@Composable
fun QuizPreStart(
    quiz              : Quiz,
    quizNavController : NavController,
    homeNavController : NavController
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 200.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = "You are about to start ${quiz.quizName}.", textAlign = TextAlign.Center, style = MaterialTheme.typography.displaySmall)
        Row {
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    quizNavController.navigate("question?q=1") {
                        popUpTo(quizNavController.graph.id) {
                            inclusive = true
                        }
                    }
                }) {
                Text("Start quiz")
            }
            OutlinedButton(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    homeNavController.navigate("studentHome") {
                        popUpTo(homeNavController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Text("Go back")
            }
        }
    }
}