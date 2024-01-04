package com.johnshrubb.stanwayeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.johnshrubb.stanwayeducation.otherComposables.ChooseLoginScreen
import com.johnshrubb.stanwayeducation.otherComposables.StudentHomeScreen
import com.johnshrubb.stanwayeducation.otherComposables.StudentLoginScreen
import com.johnshrubb.stanwayeducation.otherComposables.TutorLoginScreen
import com.johnshrubb.stanwayeducation.quizComposables.OutOfTime
import com.johnshrubb.stanwayeducation.quizComposables.QuestionCorrect
import com.johnshrubb.stanwayeducation.quizComposables.QuestionIncorrect
import com.johnshrubb.stanwayeducation.quizComposables.QuestionScreen
import com.johnshrubb.stanwayeducation.quizComposables.QuizFinished
import com.johnshrubb.stanwayeducation.quizComposables.QuizPreStart
import com.johnshrubb.stanwayeducation.quizComposables.QuizState
import com.johnshrubb.stanwayeducation.ui.theme.AppTheme
import kotlin.math.ceil

val accountManager = AccountManager()

// Used to determine the height of the top system bar. This is used by the StatusBarBG function to create a block that is theoretically the height of the status bar to achieve an effect where the status bar has a different colour.
// Not required for top bars in scaffolds as that does this for me.
var statusBarHeight: Dp = 0.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    statusBarHeight =
                        ceil((19 * LocalContext.current.resources.displayMetrics.density).toDouble()).dp
                    ChooseLoginNav()
                }
            }
        }
    }
}

/**
 * Create a full width row the height of the status bar with the effect of changing the status bar colour.
 * Use this when you don't have another element determining the colour of the status bar anyway.
 * When it is called with no argument it defaults to transparent so text is not drawn underneath.
 */
@Composable
fun StatusBarBG(color: Color = Color.Transparent) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .height(statusBarHeight)
    ) {}
}

// The root navigation controller.
@Composable
fun ChooseLoginNav() {
    val rootNavController = rememberNavController()
    NavHost(rootNavController, startDestination = "accTypeChoose") {
        // Very first composable. Allows user to pick what account type they want to log in with.
        composable("accTypeChoose") {
            ChooseLoginScreen(rootNavController)
        }

        // Student login screen.
        composable("studentLogin") {
            StudentLoginScreen(rootNavController)
        }

        // THIS APP IS A PROTOTYPE AND TUTOR LOGINS ARE NOT IMPLEMENTED
        composable("tutorLogin") {
            TutorLoginScreen(rootNavController)
        }

        // Student's home. A separate nav controller is used here.
        composable("studentHome") {
            StudentNavigation(rootNavController)
        }
    }
}

/**
 * A composable for navigating effectively through a quiz.
 * A quiz state class is used to track certain variables like the current score and quiz between different composables.
 */
@Composable
fun QuizNavigation(homeNavController: NavController, quiz: Quiz) {
    val quizNavController = rememberNavController()
    val quizState = QuizState()

    NavHost(navController = quizNavController, startDestination = "preStart") {
        // The "You are about to start X quiz" screen to avoid accidental taps.
        composable("preStart") {
            QuizPreStart(
                quiz = quiz,
                quizNavController = quizNavController,
                homeNavController = homeNavController
            )
        }

        // The actual question screen.
        composable("question?q={questionNum}") { navBackStackEntry ->
            QuestionScreen(
                quiz = quiz,
                quizNavController = quizNavController,
                navBackStackEntry = navBackStackEntry,
                quizState = quizState
            )
        }

        // Shown when a user gets the question correct.
        composable("questionCorrectScreen") {
            QuestionCorrect(
                quizNavController = quizNavController,
                quizState = quizState
            )
        }

        // Shown when a user gets a question wrong. Shows the correct answer for the question.
        composable("questionIncorrectScreen") {
            val currentQuestion by remember { mutableIntStateOf(quizState.currentQuestion) }
            val question = quiz.question[currentQuestion - 2]
            QuestionIncorrect(
                quizNavController = quizNavController,
                quizState = quizState,
                question = question,
                quiz = quiz,
            )
        }

        // Shown when a user finishes the quiz. Shows their score and goes back to the home screen.
        composable("quizFinishedScreen") {
            QuizFinished(homeNavController = homeNavController, quizState = quizState)
        }

        // Shown when a user runs out of time on a question.
        composable("outOfTime") {
            OutOfTime(
                quizNavController = quizNavController,
                question = quiz.question[quizState.currentQuestion - 1],
                quizState = quizState,
                quiz = quiz,
            )
        }
    }
}

/**
 * Used for navigation around the student's home screen.
 * Were the app not just a prototype there would be more to it.
 */
@Composable
fun StudentNavigation(rootNavController: NavController) {
    val account = accountManager.getAccount()
    val navController = rememberNavController()
    val quizManager = QuizManager(accountManager) // The primary instance of the quiz manager. Pass this around.

    // Ensure the user has not somehow got to the student home screen without a valid account.
    if (account != null) {
        NavHost(navController, startDestination = "studentHome") {
            // Just the student's home.
            composable("studentHome") {
                StudentHomeScreen(rootNavController, navController, quizManager)
            }

            // For when a student starts a quiz. Should go through a pre-start composable first.
            composable("quiz?id={id}") {
                // Ensure the app doesn't error if it tries to navigate to a non existent quiz.
                val quizID = it.arguments?.getString("id")
                if (quizID == null || quizManager.getQuiz(quizID) == null) {
                    navController.navigate("studentHome") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                // The amount of non null assertions is disgusting.
                val quiz = quizManager.getQuiz(quizID!!)
                QuizNavigation(navController, quiz!!)
            }
        }
    } else {
        // Just go back to the account type choose menu if the account is invalid.
        rootNavController.navigate("accTypeChoose") {
            popUpTo(rootNavController.graph.id) {
                inclusive = true
            }
        }
    }
}