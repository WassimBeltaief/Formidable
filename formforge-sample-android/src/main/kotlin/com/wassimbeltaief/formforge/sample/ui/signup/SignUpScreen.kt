package com.wassimbeltaief.formforge.sample.ui.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wassimbeltaief.formforge.compose.FormForge
import com.wassimbeltaief.formforge.sample.R

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val submitted by viewModel.submitted.collectAsState()

    val usernameState by viewModel.controller.username.collectAsState()
    val firstNameState by viewModel.controller.firstName.collectAsState()
    val lastNameState by viewModel.controller.lastName.collectAsState()
    val acceptTermsState by viewModel.controller.acceptTerms.collectAsState()

    if (submitted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
        ) {
            Text(
                text = stringResource(R.string.signup_success_title, viewModel.controller.data.firstName),
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.signup_success_body),
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
            text = stringResource(R.string.signup_title),
            style = MaterialTheme.typography.displaySmall,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.signup_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(40.dp))

        FormForge {
            StringField(
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
            StringField(
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
            StringField(
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
            Spacer(Modifier.height(8.dp))
            BooleanField(
                state = acceptTermsState,
                onCheckedChange = { viewModel.controller.updateAcceptTerms(it) },
                onFocusLost = { viewModel.controller.touchAcceptTerms() },
            ) {
                Column(modifier = modifier) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                        Text(label, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (showError) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { viewModel.submitForm() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.signup_submit))
        }
    }
}
