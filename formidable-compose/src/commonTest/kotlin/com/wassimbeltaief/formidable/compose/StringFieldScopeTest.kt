package com.wassimbeltaief.formidable.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StringFieldScopeTest {
    private fun scope(
        value: String = "",
        label: String = "Email",
        hint: String = "",
        isOptional: Boolean = false,
        isValidating: Boolean = false,
        showError: Boolean = false,
        errorMessage: String? = null,
    ) = StringFieldScope(
        value = value,
        onValueChange = {},
        onFocusLost = {},
        label = label,
        hint = hint,
        isOptional = isOptional,
        isValidating = isValidating,
        showError = showError,
        errorMessage = errorMessage,
        modifier = Modifier,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    @Test
    fun `value is stored correctly`() {
        assertEquals("hello@example.com", scope(value = "hello@example.com").value)
    }

    @Test
    fun `label is stored correctly`() {
        assertEquals("Email", scope(label = "Email").label)
    }

    @Test
    fun `hint is stored correctly`() {
        assertEquals("Enter email", scope(hint = "Enter email").hint)
    }

    @Test
    fun `isOptional is stored correctly`() {
        assertEquals(true, scope(isOptional = true).isOptional)
        assertEquals(false, scope(isOptional = false).isOptional)
    }

    @Test
    fun `isValidating is stored correctly`() {
        assertEquals(true, scope(isValidating = true).isValidating)
        assertEquals(false, scope(isValidating = false).isValidating)
    }

    @Test
    fun `showError is stored correctly`() {
        assertEquals(true, scope(showError = true).showError)
        assertEquals(false, scope(showError = false).showError)
    }

    @Test
    fun `errorMessage is stored when provided`() {
        assertEquals("Invalid email", scope(errorMessage = "Invalid email").errorMessage)
    }

    @Test
    fun `errorMessage is null by default`() {
        assertNull(scope().errorMessage)
    }
}
