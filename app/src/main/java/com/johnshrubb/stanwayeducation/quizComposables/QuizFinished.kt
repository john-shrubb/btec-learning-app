package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuizFinished(
    homeNavController : NavController,
    quizState         : QuizState
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 120.dp)
    ) {
        val quizScore by remember { mutableIntStateOf(quizState.currentScore) }
        Icon(
            imageVector = Icons.Outlined.StarOutline,
            contentDescription = "Congrats!",
            modifier = Modifier.size(200.dp),
            tint = Color(0xFFDE970B)
        )
        val congratulatoryText = if (quizScore <= 3) {
            "Maybe next time!"
        } else if (quizScore <= 7) {
            "Great job!"
        } else {
            "You did amazing!"
        }
        // Congratulatory message.
        Text(
            text = congratulatoryText,
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