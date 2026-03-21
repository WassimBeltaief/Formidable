package com.wassimbeltaief.formforge.sample.ui.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wassimbeltaief.formforge.compose.FormForge

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val submitted by viewModel.submitted.collectAsState()

    val usernameState by viewModel.controller.username.collectAsState()
    val firstNameState by viewModel.controller.firstName.collectAsState()
    val lastNameState by viewModel.controller.lastName.collectAsState()

    if (submitted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
        ) {
            Text(
                text = "Welcome,\n${viewModel.controller.data.firstName}.",
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Your account has been created.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 64.dp),
    ) {
        Text(
            text = "Create account",
            style = MaterialTheme.typography.displaySmall,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Fill in the details below to get started.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(40.dp))

        FormForge {
            Field(
                state = usernameState,
                onValueChange = { viewModel.controller.updateUsername(it) },
                onFocusLost = { viewModel.controller.touchUsername() },
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(label) },
                    placeholder = { Text(hint) },
                    isError = showError,
                    supportingText = if (showError) {
                        { Text(errorMessage ?: "") }
                    } else null,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = true,
                )
            }
            Spacer(Modifier.height(16.dp))
            Field(
                state = firstNameState,
                onValueChange = { viewModel.controller.updateFirstName(it) },
                onFocusLost = { viewModel.controller.touchFirstName() },
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(label) },
                    placeholder = { Text(hint) },
                    isError = showError,
                    supportingText = if (showError) {
                        { Text(errorMessage ?: "") }
                    } else null,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = true,
                )
            }
            Spacer(Modifier.height(16.dp))
            Field(
                state = lastNameState,
                onValueChange = { viewModel.controller.updateLastName(it) },
                onFocusLost = { viewModel.controller.touchLastName() },
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(label) },
                    placeholder = { Text(hint) },
                    isError = showError,
                    supportingText = if (showError) {
                        { Text(errorMessage ?: "") }
                    } else null,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = true,
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { viewModel.submitForm() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Create account")
        }
    }
}
