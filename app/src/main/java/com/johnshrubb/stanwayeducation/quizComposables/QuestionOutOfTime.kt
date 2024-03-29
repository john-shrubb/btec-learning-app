package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.Question
import com.johnshrubb.stanwayeducation.Quiz

@Composable
fun OutOfTime(
    quizNavController : NavController,
    quizState         : QuizState,
    question          : Question,
    quiz              : Quiz,
) {
    val hasValidSecondLife by remember { mutableStateOf(quizState.hasSecondLife && quiz.level < 2) }

    fun quizContinue() {
        if (hasValidSecondLife) {
            quizState.currentQuestion--
            quizState.hasSecondLife = false
            quizNavController.navigate("question?q=${quizState.currentQuestion}") {
                popUpTo(quizNavController.graph.id)
            }

        } else if (quizState.currentQuestion == 11) {
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

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 200.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = "Maybe next time!",
            modifier = Modifier.size(200.dp),
            tint = Color(0xFFFFA500)
        )

        Text(
            text = "Sorry! You ran out of time!",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        // Correct working I commented this up in ./QuestionIncorrect.kt I'm not doing it again
        if (!hasValidSecondLife) {
            Text(
                text = "${question.firstNum} ${
                    question.operation.toString().replace("*", "x")
                } ${question.secondNum} = ${
                    question.correctAnswer.toString().replace(".0", "").replace(".5", "½")
                }",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = {
                quizContinue()
            }
        ) {
            Text(if (hasValidSecondLife) "Give it another go" else "Continue")
        }
    }
}