package com.johnshrubb.stanwayeducation.otherComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.QuizManager
import com.johnshrubb.stanwayeducation.accountManager

/**
 * The user's home screen. They can generate and take quizzes from here.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    rootNavController : NavController,
    homeNavController : NavController,
    quizManager       : QuizManager
) {
    val account = accountManager.getAccount()
    Scaffold (
        topBar = {
            // The top bar. Simple displays "Your home" and has a logout button.
            TopAppBar(
                title = {
                    Text("Your Home")
                },
                actions = {
                    IconButton(onClick = {
                        accountManager.logOut()
                        rootNavController.navigate("accTypeChoose") {
                            popUpTo(rootNavController.graph.id) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Log out")
                    }
                },
                // Manually set the colours to something a bit more poppy.
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) {
        // The actual home screen
        Column(modifier = Modifier
            .padding(it) // Does everything REALLY need to be marked as a giant error if I don't have this?
            .padding(20.dp)
        ) {
            Row {
                Text(text = "Hello ${account!!.displayName.split(" ")[0]}!", style = MaterialTheme.typography.displaySmall)
            }

            // Generate new quiz buttons. Simple creates them and adds them to the list of quizzes.
            // Maybe I could do this as a dropdown and a + button

            Button(
                onClick = {
                    val quiz = quizManager.generateQuiz(0, "New Easy Quiz")
                    quizManager.quizzes += quiz
                },
                modifier = Modifier.padding(bottom = 10.dp),
            ) {
                Text(text = "Generate Easy Quiz")
            }
            Button(
                onClick = {
                    val quiz = quizManager.generateQuiz(1, "New Medium Quiz")
                    quizManager.quizzes += quiz
                },
                modifier = Modifier.padding(bottom = 10.dp),
            ) {
                Text(text = "Generate Medium Quiz")
            }
            Button(
                onClick = {
                    val quiz = quizManager.generateQuiz(2, "New Hard Quiz")
                    quizManager.quizzes += quiz
                },
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(text = "Generate Hard Quiz")
            }
            LazyColumn (
                content = {
                    // Display all the current quizzes below the quiz generation buttons.
                    // Android updates this automatically when the quiz list gets added to.
                    items(quizManager.quizzes) { quiz ->
                        // The actual clickable card
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .clickable {
                                    homeNavController.navigate("quiz?id=${quiz.quizID}") {
                                        popUpTo(homeNavController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }
                        ) {
                            // Column with quiz details.
                            // It has the difficulty and the name of the quiz, maybe I could make a best score.
                            Column(modifier = Modifier.padding(15.dp)) {
                                Text(quiz.quizName, style = MaterialTheme.typography.headlineSmall)
                                Text(
                                    text = if (quiz.level == 0) "Easy" else if (quiz.level == 1) "Medium" else "Hard",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}