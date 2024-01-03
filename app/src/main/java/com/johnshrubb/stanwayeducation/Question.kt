package com.johnshrubb.stanwayeducation

import androidx.core.text.isDigitsOnly
import kotlin.random.Random

/**
 * The Question Class.
 * This class is intended to hold all the questions that will be used in the quiz which the user will have to work through.
 * The ID is automatically generated and should only be manually defined if you are pulling a quiz class from the database.
 * The possibles answers list should look like this
 * ```
 * listOf(4) (
 *      9,
 *      13,
 *      20,
 *      3
 * )
 * ```
 */
class Question (
    @Suppress("MemberVisibilityCanBePrivate", "PropertyName")
    val ID : String = List(15) {Random.nextInt(10)}.joinToString(""),
    val operation : Char,
    val firstNum : Int,
    val secondNum : Int,
    val possibleAnswers : List<Float>,
    var correctAnswer : Float = 0F
) {
    init {
        if (!ID.isDigitsOnly()) {
            throw Error("Invalid ID. Must be 15 digits.")
        }

        // For simplicity's sake only 4 operators are allowed.

        if (possibleAnswers.size != 4) {
            throw Error("There must be 4 possible answers passed into the question.")
        }

        // The following is a part of the requirement spec:
        //      - The allowed operators are:
        //          - Add      (+)
        //          - Subtract (-)
        //          - Multiply (*)
        //          - Divide   (/)
        //     - Both numbers in the question must be:
        //          - Integers
        //          - Between 1 and 12
        //     - The answer to the question must:
        //          - Be greater than 0
        //          - Not have a decimal value of anything other than .0 or .5

        if (firstNum < 1 || firstNum > 12 || secondNum < 1 || secondNum > 12) {
            throw Error("First and second numbers during quiz initialisation must be between 1 and 12.\nFirst number ${firstNum}\nSecond number ${secondNum}\nOperation: $operation")
        }

        // Calculate the correct answer and ensure an invalid operator hasn't been passed in.

        when(operation) {
            '+' -> {
                correctAnswer = (firstNum + secondNum).toFloat()
            }
            '-' -> {
                correctAnswer = (firstNum - secondNum).toFloat()
            }
            '/' -> {
                correctAnswer = firstNum.toFloat() / secondNum.toFloat()
            }
            '*' -> {
                correctAnswer = (firstNum * secondNum).toFloat()
            }
            else -> {
                throw Error("Invalid operation in class initialisation. Must be +, -, * or /")
            }
        }

        // A question cannot have a correct answer that is under 0.
        if (correctAnswer < 0) {
            throw Error("A question must not return an answer less than 0.")
        }

        // Check that the correct answer for the question does not have a decimal value of anything other than a half.
        // The first part of the if statement checks if the answer doesn't have a decimal place
        // The second part splits the correct answer by the floating point and accesses [1] which is the right side of the decimal to check it its 5.
        if (correctAnswer.toString().split(".")[1] != "5" && correctAnswer.toString().split(".")[1] != "0") {
            throw Error("Attempted to initialise question with $correctAnswer as correct answer. Decimal places can not be more complex than a half.")
        }
    }
}