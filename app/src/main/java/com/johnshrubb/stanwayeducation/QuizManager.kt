package com.johnshrubb.stanwayeducation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

class QuizManager(accountManager: AccountManager) {
    var quizzes by
        mutableStateOf(
            listOf(
                generateQuiz(0, "Andy's Homework Quiz"),
                generateQuiz(1, "Richard's Harder Quiz"),
                generateQuiz(2, "Steve's Impossible Quiz")
            )
        )

    fun getQuiz(id : String) : Quiz? {
        quizzes.forEach { quiz ->
            if (quiz.quizID == id) return quiz
        }
        return null
    }

    fun generateQuiz(level : Int, quizName : String) : Quiz {
        // Validate the level.

        if (level < 0 || level > 2) {
            throw Error("Tried to generate quiz with unsupported level.")
        }
        val questionList = List(10) {
            // Valid operators that can be used to make a question
            val operators = listOf('+', '-', '*', '/')

            // Both numbers
            val firstNum : Int
            var secondNum : Int

            // Possible answers for the question
            val possibleAnswers : List<Float>

            // The correct answer for the question
            var correctAnswer : Float

            // The selected operator that should be used in the question
            val selectedOperator = operators[Random.nextInt(until = operators.size)]

            // Switch statement for the operators to determine how to generate the numbers.
            when (selectedOperator) {
                '+' -> {
                    firstNum = Random.nextInt(from = 1, 13)
                    secondNum = Random.nextInt(from = 1, 13)


                    correctAnswer = (firstNum + secondNum).toFloat()
                }
                '-' -> {
                    firstNum = Random.nextInt(from = 4, until = 13)
                    secondNum = Random.nextInt(from = 1, until = firstNum)

                    correctAnswer = (firstNum - secondNum).toFloat()
                }
                // ½ symbol should be used when displaying possible answers.
                '/' -> {
                    firstNum = Random.nextInt(from = 4, until = 13)
                    if (firstNum % 2 == 1) {
                        secondNum = 2
                        correctAnswer = firstNum.toFloat() / secondNum.toFloat()
                    } else {
                        correctAnswer = 0F
                        while(true) {
                            secondNum = Random.nextInt(from = 1, until = firstNum)
                            correctAnswer = firstNum.toFloat() / secondNum.toFloat()

                            if (correctAnswer.toString().endsWith(".0") || correctAnswer.toString().endsWith(".5")) {
                                break
                            }
                        }
                    }
                }
                '*' -> {
                    firstNum = Random.nextInt(from = 1, until = 12)
                    secondNum = Random.nextInt(from = 1, until = 12)

                    correctAnswer = (firstNum * secondNum).toFloat()
                }
                else -> {
                    throw Error("Invalid operator '${selectedOperator}' used in question construction.")
                }
            }

            // Insert the correct answer at a random point in the correct answers list.
            val positionInArray = Random.nextInt(0, 4)
            // Make a temporary list of the possible answers to make sure there are no repeated answers.
            val possibleAnswersTemp = mutableListOf<Float>()
            possibleAnswers = List(4) {
                if (it == positionInArray) correctAnswer
                else {
                    var gennedAnswer = Random.nextInt(from = (correctAnswer - 4).toInt(), until = (correctAnswer + 4).toInt()).toFloat()
                    if (selectedOperator == '/' && Random.nextBoolean()) {
                        gennedAnswer += 0.5F
                    }
                    // Keep generating possible answers until a sensible answer is achieved.
                    while (gennedAnswer == correctAnswer || possibleAnswersTemp.contains(gennedAnswer) || gennedAnswer <= 0.5) {
                        gennedAnswer = Random.nextInt(from = (correctAnswer - 4).toInt(), until = (correctAnswer + 4).toInt()).toFloat()
                        if (selectedOperator == '/' && Random.nextBoolean()) {
                            gennedAnswer += 0.5F
                        }
                    }
                    // Add the generated answer to the list of temporary possible answers and add it to the actual possible answers list.
                    possibleAnswersTemp += gennedAnswer
                    gennedAnswer
                }
            }

            // Return the question for the list.
            Question(
                operation = selectedOperator,
                firstNum = firstNum,
                secondNum = secondNum,
                possibleAnswers = possibleAnswers,
                correctAnswer = correctAnswer
            )
        }

        // Return the created quiz.

        return Quiz(
            quizName = quizName.ifBlank { "A quiz made for you" },
            level = level,
            question = questionList
        )
    }
}