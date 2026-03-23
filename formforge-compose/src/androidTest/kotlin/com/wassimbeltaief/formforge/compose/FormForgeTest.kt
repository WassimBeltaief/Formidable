package com.wassimbeltaief.formforge.compose

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.wassimbeltaief.formforge.core.state.FieldState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormForgeTest {

    @get:Rule(order = 0)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.POST_NOTIFICATIONS
    )

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<ComponentActivity>()

    private fun fieldState(
        value: String = "",
        errors: List<String> = emptyList(),
        isTouched: Boolean = false,
        label: String = "",
        hint: String = "",
    ) = FieldState(
        value = value,
        initialValue = value,
        errors = errors,
        isTouched = isTouched,
        label = label,
        hint = hint,
    )

    // --- FieldScope content is rendered ---

    @Test
    fun fieldScopeContent_isRendered() {
        rule.setContent {
            FormForge {
                StringField(
                    state = fieldState(value = "hello"),
                    onValueChange = {},
                    onFocusLost = {},
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                    )
                }
            }
        }
        rule.waitForIdle()
        rule.onNodeWithTag("field").assertTextContains("hello")
    }

    // --- ImeAction: single field gets Done ---

    @Test
    fun singleField_hasImeActionDone() {
        var capturedImeAction: ImeAction? = null
        rule.setContent {
            FormForge {
                StringField(
                    state = fieldState(),
                    onValueChange = {},
                    onFocusLost = {},
                ) {
                    capturedImeAction = keyboardOptions.imeAction
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }
            }
        }
        rule.waitForIdle()
        assertEquals(ImeAction.Done, capturedImeAction)
    }

    // --- ImeAction: first of two fields — pressing IME Next moves focus to field2 ---

    @Test
    fun twoFields_imeNextOnFirst_movesFocusToSecond() {
        var field2Focused = false
        rule.setContent {
            FormForge {
                StringField(state = fieldState(), onValueChange = {}, onFocusLost = {}) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field1"),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }
                StringField(state = fieldState(), onValueChange = {}, onFocusLost = {}) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier
                            .testTag("field2")
                            .onFocusChanged { if (it.isFocused) field2Focused = true },
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }
            }
        }
        rule.onNodeWithTag("field1").performClick()
        rule.onNodeWithTag("field1").performImeAction()
        rule.waitForIdle()
        assertTrue(field2Focused)
    }

    // --- onValueChange is called when value changes ---

    @Test
    fun onValueChange_isCalled() {
        var state by mutableStateOf(fieldState(value = ""))
        rule.setContent {
            FormForge {
                StringField(
                    state = state,
                    onValueChange = { state = state.copy(value = it) },
                    onFocusLost = {},
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }
            }
        }
        rule.onNodeWithTag("field").performClick()
        rule.onNodeWithTag("field").performTextInput("abc")
        rule.waitForIdle()
        assertEquals("abc", state.value)
    }

    // --- showError is false when not touched, true when touched with errors ---

    @Test
    fun showError_falseWhenNotTouched() {
        var capturedShowError: Boolean? = null
        rule.setContent {
            FormForge {
                StringField(
                    state = fieldState(errors = listOf("Required"), isTouched = false),
                    onValueChange = {},
                    onFocusLost = {},
                ) {
                    capturedShowError = showError
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                    )
                }
            }
        }
        rule.waitForIdle()
        assertEquals(false, capturedShowError)
    }

    @Test
    fun showError_trueWhenTouchedWithErrors() {
        var capturedShowError: Boolean? = null
        var capturedErrorMessage: String? = null
        rule.setContent {
            FormForge {
                StringField(
                    state = fieldState(errors = listOf("Required"), isTouched = true),
                    onValueChange = {},
                    onFocusLost = {},
                ) {
                    capturedShowError = showError
                    capturedErrorMessage = errorMessage
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                    )
                }
            }
        }
        rule.waitForIdle()
        assertEquals(true, capturedShowError)
        assertEquals("Required", capturedErrorMessage)
    }

    // --- label and hint are surfaced in FieldScope ---

    @Test
    fun labelAndHint_surfacedInFieldScope() {
        var capturedLabel: String? = null
        var capturedHint: String? = null
        rule.setContent {
            FormForge {
                StringField(
                    state = fieldState(label = "Email", hint = "Enter your email"),
                    onValueChange = {},
                    onFocusLost = {},
                ) {
                    capturedLabel = label
                    capturedHint = hint
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                    )
                }
            }
        }
        rule.waitForIdle()
        assertEquals("Email", capturedLabel)
        assertEquals("Enter your email", capturedHint)
    }

    // --- wasFocused guard: onFocusLost does NOT fire if field was never focused ---

    @Test
    fun onFocusLost_notCalledIfNeverFocused() {
        var focusLostCalled = false
        rule.setContent {
            FormForge {
                StringField(
                    state = fieldState(),
                    onValueChange = {},
                    onFocusLost = { focusLostCalled = true },
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                    )
                }
            }
        }
        rule.waitForIdle()
        assertEquals(false, focusLostCalled)
    }

    // --- focusRequesters.clear() on recomposition: fields re-register correctly ---

    @Test
    fun focusRequesters_reregisteredOnRecomposition() {
        var field2Focused = false
        var recompose by mutableStateOf(false)
        rule.setContent {
            // Reading recompose triggers recomposition of FormForge when it changes
            if (recompose) Unit
            FormForge {
                StringField(state = fieldState(), onValueChange = {}, onFocusLost = {}) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field1"),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }
                StringField(state = fieldState(), onValueChange = {}, onFocusLost = {}) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier
                            .testTag("field2")
                            .onFocusChanged { if (it.isFocused) field2Focused = true },
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }
            }
        }
        // Trigger a recomposition
        recompose = true
        rule.waitForIdle()
        // Focus traversal should still work after re-registration
        rule.onNodeWithTag("field1").performClick()
        rule.onNodeWithTag("field1").performImeAction()
        rule.waitForIdle()
        assertTrue(field2Focused)
    }

    // --- Value updates propagate to FieldScope.value ---

    @Test
    fun valueUpdate_propagatesToFieldScope() {
        var state by mutableStateOf(fieldState(value = "initial"))
        rule.setContent {
            FormForge {
                StringField(
                    state = state,
                    onValueChange = { state = state.copy(value = it) },
                    onFocusLost = {},
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier.testTag("field"),
                    )
                }
            }
        }
        rule.onNodeWithTag("field").assertTextContains("initial")
        state = state.copy(value = "updated")
        rule.waitForIdle()
        rule.onNodeWithTag("field").assertTextContains("updated")
    }
}
