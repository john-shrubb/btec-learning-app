package com.johnshrubb.stanwayeducation

import androidx.core.text.isDigitsOnly
import kotlin.random.Random

/**
 * Account Class
 *
 * Very basic account class used for login.
 * If the UserID variable is going to be manually assigned use 15 digits in the form of a string. Otherwise the class will throw an error.
 */
class Account (
    val userID       : String = List(15) {Random.nextInt(10)}.joinToString(""),
    val userName     : String,
    val displayName  : String,
    val email        : String,
    val password     : String,
    val tutorAccount : Boolean = false
) {
    init {
        // Check an invalid user ID is not passed into the account
        if (userID.length != 15 || !userID.isDigitsOnly()) {
            throw Error("Invalid user ID passed for initialisation of account class.")
        }
    }
}