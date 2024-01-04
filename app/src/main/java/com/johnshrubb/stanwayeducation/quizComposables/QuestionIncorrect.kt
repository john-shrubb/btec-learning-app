package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
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
fun QuestionIncorrect(
    quizNavController : NavController,
    quizState         : QuizState,
    question          : Question,
    quiz              : Quiz,
) {
    val mutableQuestion by remember { mutableStateOf(question) }
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
            quizState.hasSecondLife = true
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
        if (!hasValidSecondLife) {
            Text(
                text = "${mutableQuestion.firstNum}" +
                        // I hate this has to be in a string because of those spaces.
                        " ${
                            mutableQuestion.operation.toString().replace("*", "x")
                        } " + // The correct operator with * changed to x for user friendliness
                        "${mutableQuestion.secondNum}" +
                        " = " +
                        mutableQuestion.correctAnswer.toString()
                            // These two replace methods make the float values look more attractive by removing the .0 you get when converting them to a string
                            .replace(".0", "")
                            .replace(".5", "½"), // Easier for kids ig
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