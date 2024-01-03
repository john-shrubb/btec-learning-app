package com.johnshrubb.stanwayeducation.otherComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.DeveloperMode
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.johnshrubb.stanwayeducation.Account
import com.johnshrubb.stanwayeducation.StatusBarBG
import com.johnshrubb.stanwayeducation.accountManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StudentLoginScreen(
    rootNavController : NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // Set the status bar colour.
        StatusBarBG(color = MaterialTheme.colorScheme.primary)
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {
            var accountIdentifier by remember { mutableStateOf("") }
            var accountPassword by remember { mutableStateOf("") }
            // The large friendly looking "Login" Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Login", style = MaterialTheme.typography.headlineLarge)
            }

            // And some more filler text to really give the illusion of being a real app.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "If you do not remember your password please talk to your teacher.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            // this makes me frown
            val autofill = LocalAutofill.current

            // The username and email autofill stuff. I literally could not figure out how to make it save the email of password.
            val emailNode = AutofillNode(
                autofillTypes = listOf(AutofillType.EmailAddress, AutofillType.Username),
                onFill = {
                    accountIdentifier = it
                }
            )

            LocalAutofillTree.current += emailNode

            val passwordNode = AutofillNode(
                autofillTypes = listOf(AutofillType.Password),
                onFill = {
                    accountPassword = it
                }
            )

            LocalAutofillTree.current += passwordNode

            // The account identifier field. The user can enter an email OR username cus I'm that sophisticated.
            OutlinedTextField(
                value = accountIdentifier,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .onGloballyPositioned {
                        emailNode.boundingBox = it.boundsInWindow()
                    }
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) {
                                requestAutofillForNode(emailNode)
                            } else {
                                cancelAutofillForNode(emailNode)
                            }
                        }
                    },
                onValueChange = {
                    accountIdentifier = it
                },
                label = {
                    Text("Username or email")
                },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null)
                }
            )
            OutlinedTextField(
                value = accountPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    // Idrk what this does but autofill breaks without this.
                    .onGloballyPositioned {
                        passwordNode.boundingBox = it.boundsInWindow()
                    }
                    // The autofill stuff.
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) {
                                requestAutofillForNode(passwordNode)
                            } else {
                                cancelAutofillForNode(passwordNode)
                            }
                        }
                    },
                onValueChange = {
                    accountPassword = it
                },
                label = {
                    Text("Password")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null)
                }
            )

            var errorText by remember { mutableStateOf("") }
            if (errorText.isNotBlank()) {
                Text(text = errorText, color = Color.Red, modifier = Modifier.padding(top = 10.dp))
            }

            Row {
                // The login button
                Button(
                    onClick = {
                        val account: Account? =
                            accountManager.attemptToAuthenticate(
                                accountIdentifier,
                                accountPassword,
                                false
                            )
                        // Shows an error to the user if they use incorrect credentials.
                        if (account == null) {
                            errorText = "Invalid account details. Please try again."
                        } else {
                            // Or navigate to the student home if authentication is successful.
                            rootNavController.navigate("studentHome") {
                                popUpTo(rootNavController.graph.id) { // This prevents the user going backwards to the login screen
                                    inclusive = true
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 20.dp),
                ) {
                    // Just an icon for user friendly points
                    Icon(
                        Icons.Outlined.LockOpen, contentDescription = null, modifier = Modifier
                        .padding(end = 5.dp)
                        .size(17.dp)
                    )
                    Text("Login", style = MaterialTheme.typography.bodyLarge)
                }

                // If the user wants to go back and experience the nothingness on the tutor login page then they can do that.
                OutlinedButton(onClick = {
                    rootNavController.navigateUp()
                }, modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Go back",
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Purely to save me typing in a username and password literally every single time I want to test a new version of the app.
            // Logs in in exactly the same way as above but it just feeds in credentials for me.
            // Should probably be removed for production. Not that this project will ever reach that stage.
            TextButton(modifier = Modifier.padding(top = 10.dp), onClick = {
                accountManager.attemptToAuthenticate("john-shrubb", "password123", false)
                rootNavController.navigate("studentHome") {
                    popUpTo(rootNavController.graph.id) {
                        inclusive = true
                    }
                }
            }) {
                Icon(
                    Icons.Outlined.DataObject,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 7.dp)
                )
                Text("DEBUG: Log in as John")
            }
        }
    }
}