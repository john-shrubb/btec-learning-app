package com.johnshrubb.stanwayeducation

import kotlin.random.Random

/**
 * The quiz class
 */
class Quiz(
    val quizID : String = List(15) { Random.nextInt(10) }.joinToString(""),
    val quizName : String,
    // Pass 10 questions through
    val question : List<Question>,
    // Level meanings:
    //      Level 0 - No time limit on question
    //      Level 1 - 20 second time limit
    //      Level 2 - 10 second time limit
    val level : Int,
) {
    init {
        if (question.size != 10) {
            throw Error("Quiz class must have 10 questions")
        }
        if (level < 0 || level > 2) {
            throw Error("Level must be 0, 1 or 2")
        }
    }
}