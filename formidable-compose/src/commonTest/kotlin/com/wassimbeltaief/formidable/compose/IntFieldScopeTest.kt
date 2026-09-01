package com.wassimbeltaief.formidable.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IntFieldScopeTest {
    private fun scope(
        value: Int = 0,
        label: String = "Age",
        hint: String = "",
        showError: Boolean = false,
        errorMessage: String? = null,
    ) = IntFieldScope(
        value = value,
        onValueChange = {},
        label = label,
        hint = hint,
        showError = showError,
        errorMessage = errorMessage,
        modifier = Modifier,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
    )

    @Test
    fun `value reflects provided int`() {
        assertEquals(25, scope(value = 25).value)
    }

    @Test
    fun `value defaults to zero`() {
        assertEquals(0, scope().value)
    }

    @Test
    fun `label is stored`() {
        assertEquals("Age", scope(label = "Age").label)
    }

    @Test
    fun `hint is stored`() {
        assertEquals("Enter your age", scope(hint = "Enter your age").hint)
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
        assertEquals("Must be 18+", scope(showError = true, errorMessage = "Must be 18+").errorMessage)
    }
}
