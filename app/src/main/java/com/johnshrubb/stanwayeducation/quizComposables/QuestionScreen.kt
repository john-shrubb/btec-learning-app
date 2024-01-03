package com.johnshrubb.stanwayeducation.quizComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.Quiz
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**
 * The question screen composable.
 * Displays the question and allows the user to answer. Correctly passes off to the correct composable after the user has (Or hasn't) selected an answer.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    quiz : Quiz,
    quizNavController : NavController,
    navBackStackEntry: NavBackStackEntry,
    quizState : QuizState
) {
    // Grab a few values like the current question number and the question object.
    // TBF this composable in the navigation doesn't really need a ?q=X on the end of it anymore as I can just use quiz state.
    // Maybe later I will fix that...
    val questionNum = navBackStackEntry.arguments?.getString("questionNum")!!.toInt()
    val question = quiz.question[questionNum - 1]

    // All the buttons just call this function which handles everything from there.
    fun handleAnswerInput(answer : Float) {
        quizState.currentQuestion++
        if (question.correctAnswer == answer) {
            quizState.currentScore++
            quizNavController.navigate("questionCorrectScreen") {
                popUpTo(quizNavController.graph.id) {
                    inclusive = true
                }
            }
        } else {
            quizNavController.navigate("questionIncorrectScreen") {
                popUpTo(quizNavController.graph.id) {
                    inclusive = true
                }
            }
        }
    }

    // The time remaining. Is effectively infinite on level 0.
    var timeRemaining by remember {
        mutableIntStateOf(if (quiz.level == 0) -1 else if (quiz.level == 1) 20 else 10)
    }

    // A separate variable is required for what is displayed in case the user is on level 0
    val timeToDisplay = if (timeRemaining <= -1) "Infinite" else timeRemaining.toString()

    // I'm using a scaffold here for the TopAppBar.
    // It also automatically sets the top system bar colour..
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Question $questionNum")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            // The timer.
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1.seconds)
                    timeRemaining -= 1
                    if (timeRemaining == 0) {
                        quizState.currentQuestion++
                        quizNavController.navigate("outOfTime") {
                            popUpTo(quizNavController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
            ) {
                // Current score for the quiz.
                Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1F)) {
                    Text(text = "Score: ${quizState.currentScore}", style = MaterialTheme.typography.titleLarge)
                }
                // And the timer.
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1F)) {
                    Row {
                        Icon(
                            Icons.Outlined.Timer,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Text(text = timeToDisplay)
                    }
                }
            }
            // The actual question is displayed here
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()) {
                Text(text = "What is", style = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center)
                Text(text = "${question.firstNum} ${question.operation.toString().replace('*', 'x')} ${question.secondNum}?", style = MaterialTheme.typography.displayLarge, textAlign = TextAlign.Center)
            }
            // This row is only necessary to make the .weight(1F) function work and I hate it.
            Row {
                @Composable
                fun possibleAnswerCard(answer: Float) {
                    // Prettied answer is just a more formatted version with .0 removed and .5 replaced with a unicode fraction.
                    val prettiedAnswer = answer
                        .toString()
                        .replace(".0", "")
                        .replace(".5", "½")

                    // actual card which displays the answer.
                    Card(modifier = Modifier
                        .padding(10.dp)
                        .weight(1F)
                        .clickable {
                            handleAnswerInput(answer)
                        }
                    ) {
                        Text(
                            text = prettiedAnswer,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                // a LOT of padding for the text inside the card.
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 30.dp,
                                    bottom = 30.dp
                                )
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                // The 4 possible answers displayed in two rows.
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        question.possibleAnswers.dropLast(2).forEach { answer -> // Basically just "Do this two times." but twice.
                            possibleAnswerCard(answer = answer)
                        }
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        question.possibleAnswers.drop(2).forEach { answer ->
                            possibleAnswerCard(answer = answer)
                        }
                    }
                }
            }
        }
    }
}