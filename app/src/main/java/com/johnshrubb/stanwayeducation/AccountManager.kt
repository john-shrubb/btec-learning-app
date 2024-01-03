package com.johnshrubb.stanwayeducation

class AccountManager {
    // Some default accounts that can be played with.
    private val accounts = listOf (
        Account(
            userName = "john-shrubb",
            displayName = "John Shrubb",
            email = "shrubbjohn@gmail.com",
            password = "password123", // Some passwords are more secure than others.
            tutorAccount = false
        ),
        Account(
            userName = "andy-stanway",
            displayName = "Andrew Stanway",
            email = "astanway@gmail.com",
            password = "bigSt@nway",
            tutorAccount = true
        ),
        Account(
            userName = "zach-mccracken",
            displayName = "Zach McCracken",
            email = "zach-mccracken@gmail.com",
            password = "B1gZachCrack",
            tutorAccount = false
        ),
    )

    // Doing accounts this way makes the account read only so the main activity cannot inject a valid account into the manager.
    private var loggedInAccount : Account? = null

    fun getAccount() : Account? {
        return loggedInAccount
    }

    fun logOut() {
        loggedInAccount = null
    }

    fun attemptToAuthenticate(
        accountIdentifier : String, // Can be username or email.
        accountPassword : String,
        tutorAuth : Boolean // Is the user trying to authenticate as a tutor? true = yes, false = no
    ): Account? {
        // The function first attempts to find an account. Authentication is checked later.
        var foundAccount : Account? = null
        for (account in accounts) {
            if (account.userName == accountIdentifier || account.email.lowercase() == accountIdentifier) {
                foundAccount = account
                break
            }
        }

        // The miserably long if else chain. Returns null if authentication is invalid.
        return if (foundAccount == null) {
            null
        } else if (foundAccount.password != accountPassword) { // Check password first
            null
        } else if (foundAccount.tutorAccount != tutorAuth) { // Then check whether the authenticating user is a tutor.
            null
        } else {
            loggedInAccount = foundAccount
            foundAccount // Return the correct account if one is found.
        }
    }

    // Unused function I'm just keeping for technical purposes.
    @Suppress("unused")
    fun getUserByIdentifier(accountIdentifier : String) : Account? {
        for (account in accounts) {
            if (account.userID == accountIdentifier || account.userName == accountIdentifier || account.email == accountIdentifier) {
                return account
            }
        }
        return null
    }
}