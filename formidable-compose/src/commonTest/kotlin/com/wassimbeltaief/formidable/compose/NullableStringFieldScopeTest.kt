package com.wassimbeltaief.formidable.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NullableStringFieldScopeTest {
    private fun scope(
        value: String = "",
        label: String = "Nickname",
        hint: String = "",
        isOptional: Boolean = true,
        isValidating: Boolean = false,
        showError: Boolean = false,
        errorMessage: String? = null,
    ) = NullableStringFieldScope(
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
    fun `value reflects provided string`() {
        assertEquals("wassim", scope(value = "wassim").value)
    }

    @Test
    fun `value empty string by default`() {
        assertEquals("", scope().value)
    }

    @Test
    fun `label is stored`() {
        assertEquals("Nickname", scope(label = "Nickname").label)
    }

    @Test
    fun `hint is stored`() {
        assertEquals("Optional", scope(hint = "Optional").hint)
    }

    @Test
    fun `isOptional true by default in this scope`() {
        assertTrue(scope().isOptional)
    }

    @Test
    fun `isOptional false when passed false`() {
        assertFalse(scope(isOptional = false).isOptional)
    }

    @Test
    fun `isValidating false by default`() {
        assertFalse(scope().isValidating)
    }

    @Test
    fun `isValidating true when passed true`() {
        assertTrue(scope(isValidating = true).isValidating)
    }

    @Test
    fun `showError false by default`() {
        assertFalse(scope().showError)
    }

    @Test
    fun `showError true when passed true`() {
        assertTrue(scope(showError = true).showError)
    }

    @Test
    fun `errorMessage null by default`() {
        assertNull(scope().errorMessage)
    }

    @Test
    fun `errorMessage stored when provided`() {
        assertEquals("Too short", scope(showError = true, errorMessage = "Too short").errorMessage)
    }
}
