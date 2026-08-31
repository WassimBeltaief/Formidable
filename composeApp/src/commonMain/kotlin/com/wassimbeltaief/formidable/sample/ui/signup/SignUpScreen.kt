package com.wassimbeltaief.formidable.sample.ui.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.wassimbeltaief.formidable.compose.BooleanField
import com.wassimbeltaief.formidable.compose.EnumField
import com.wassimbeltaief.formidable.compose.Formidable
import com.wassimbeltaief.formidable.compose.IntField
import com.wassimbeltaief.formidable.compose.NullableStringField
import com.wassimbeltaief.formidable.compose.StringField
import com.wassimbeltaief.formidable.sample.domain.model.ContactMethod

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val submitted by viewModel.submitted.collectAsState()

    val usernameState by viewModel.controller.username.collectAsState()
    val firstNameState by viewModel.controller.firstName.collectAsState()
    val lastNameState by viewModel.controller.lastName.collectAsState()
    val passwordState by viewModel.controller.password.collectAsState()
    val confirmPasswordState by viewModel.controller.confirmPassword.collectAsState()
    val nicknameState by viewModel.controller.nickname.collectAsState()
    val ageState by viewModel.controller.age.collectAsState()
    val contactMethodState by viewModel.controller.contactMethod.collectAsState()
    val emailState by viewModel.controller.email.collectAsState()
    val phoneState by viewModel.controller.phone.collectAsState()
    val acceptTermsState by viewModel.controller.acceptTerms.collectAsState()

    val isFormValid by viewModel.controller.isValid.collectAsState()

    if (submitted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
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
        }
        return
    }

    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier =
                    Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
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

                Formidable {
                    // async validation: slot overrides via named `config` arg
                    StringField(
                        state = usernameState,
                        onValueChange = { viewModel.controller.updateUsername(it) },
                        onFocusLost = { viewModel.controller.touchUsername() },
                        config = {
                            supportingText =
                                when {
                                    usernameState.isValidating -> {
                                        { Text("Checking availability…") }
                                    }
                                    usernameState.showError -> {
                                        { Text(usernameState.errorMessage ?: "") }
                                    }
                                    else -> null
                                }
                            trailingIcon =
                                if (usernameState.isValidating) {
                                    { CircularProgressIndicator(Modifier.size(20.dp)) }
                                } else {
                                    null
                                }
                        },
                    )
                    Spacer(Modifier.height(16.dp))
                    StringField(
                        state = firstNameState,
                        onValueChange = { viewModel.controller.updateFirstName(it) },
                        onFocusLost = { viewModel.controller.touchFirstName() },
                    )
                    Spacer(Modifier.height(16.dp))
                    StringField(
                        state = lastNameState,
                        onValueChange = { viewModel.controller.updateLastName(it) },
                        onFocusLost = { viewModel.controller.touchLastName() },
                    )
                    Spacer(Modifier.height(16.dp))

                    StringField(
                        state = passwordState,
                        onValueChange = { viewModel.controller.updatePassword(it) },
                        onFocusLost = { viewModel.controller.touchPassword() },
                        config = {
                            visualTransformation = PasswordVisualTransformation()
                            keyboardType = KeyboardType.Password
                        },
                    )
                    Spacer(Modifier.height(16.dp))
                    StringField(
                        state = confirmPasswordState,
                        onValueChange = { viewModel.controller.updateConfirmPassword(it) },
                        onFocusLost = { viewModel.controller.touchConfirmPassword() },
                        config = {
                            visualTransformation = PasswordVisualTransformation()
                            keyboardType = KeyboardType.Password
                        },
                    )
                    Spacer(Modifier.height(16.dp))

                    NullableStringField(
                        state = nicknameState,
                        onValueChange = { viewModel.controller.updateNickname(it) },
                        onFocusLost = { viewModel.controller.touchNickname() },
                    )
                    Spacer(Modifier.height(16.dp))
                    IntField(
                        state = ageState,
                        onValueChange = { viewModel.controller.updateAge(it) },
                        onFocusLost = { viewModel.controller.touchAge() },
                    )
                    Spacer(Modifier.height(24.dp))
                    EnumField(
                        state = contactMethodState,
                        options = ContactMethod.entries,
                        onSelect = {
                            viewModel.controller.updateContactMethod(it)
                            viewModel.controller.touchContactMethod()
                        },
                    )

                    AnimatedVisibility(visible = emailState.isVisible) {
                        Column {
                            Spacer(Modifier.height(16.dp))
                            NullableStringField(
                                state = emailState,
                                onValueChange = { viewModel.controller.updateEmail(it) },
                                onFocusLost = { viewModel.controller.touchEmail() },
                            )
                        }
                    }

                    AnimatedVisibility(visible = phoneState.isVisible) {
                        Column {
                            Spacer(Modifier.height(16.dp))
                            NullableStringField(
                                state = phoneState,
                                onValueChange = { viewModel.controller.updatePhone(it) },
                                onFocusLost = { viewModel.controller.touchPhone() },
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    BooleanField(
                        state = acceptTermsState,
                        onCheckedChange = { viewModel.controller.updateAcceptTerms(it) },
                        onFocusLost = { viewModel.controller.touchAcceptTerms() },
                    )
                }

                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.submitForm() },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Create account")
                }
            }
        }
        ScrollbarThumb(scrollState, Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
private fun ScrollbarThumb(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    if (scrollState.maxValue <= 0) return
    BoxWithConstraints(modifier = modifier.fillMaxHeight().width(6.dp)) {
        val totalPx = constraints.maxHeight + scrollState.maxValue
        val thumbFraction = constraints.maxHeight.toFloat() / totalPx
        val thumbOffset = maxHeight * (scrollState.value.toFloat() / totalPx)
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(thumbFraction)
                    .offset(y = thumbOffset)
                    .background(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        RoundedCornerShape(3.dp),
                    ),
        )
    }
}
