package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.Question

@Composable
fun QuestionIncorrect(
    quizNavController : NavController,
    quizState         : QuizState,
    question          : Question,
) {
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
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 200.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Cancel,
            contentDescription = "Maybe next time!",
            modifier = Modifier.size(200.dp),
            tint = Color.Red
        )
        // imagine getting 5 + 5 wrong smh
        Text(
            text = "Sorry! That's not right!",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
        )
        // Display some working for the correct answer.
        Text(
            text = "${question.firstNum}" +
                    // I hate this has to be in a string because of those spaces.
                    " ${question.operation.toString().replace("*", "x")} " + // The correct operator with * changed to x for user friendliness
                    "${question.secondNum}" +
                    " = " +
                    question.correctAnswer.toString()
                        // These two replace methods make the float values look more attractive by removing the .0 you get when converting them to a string
                        .replace(".0", "")
                        .replace(".5", "½"), // Easier for kids ig
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = {
                quizContinue()
            }
        ) {
            Text("Continue")
        }
    }
}