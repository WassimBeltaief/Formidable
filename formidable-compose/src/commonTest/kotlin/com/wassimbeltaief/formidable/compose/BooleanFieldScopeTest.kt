package com.wassimbeltaief.formidable.compose

import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BooleanFieldScopeTest {
    private fun scope(
        checked: Boolean = false,
        label: String = "Accept terms",
        hint: String = "",
        showError: Boolean = false,
        errorMessage: String? = null,
    ) = BooleanFieldScope(
        checked = checked,
        onCheckedChange = {},
        label = label,
        hint = hint,
        showError = showError,
        errorMessage = errorMessage,
        modifier = Modifier,
    )

    @Test
    fun `checked reflects provided value`() {
        assertTrue(scope(checked = true).checked)
        assertFalse(scope(checked = false).checked)
    }

    @Test
    fun `label is stored correctly`() {
        assertEquals("Accept terms", scope(label = "Accept terms").label)
    }

    @Test
    fun `hint is stored correctly`() {
        assertEquals("Optional hint", scope(hint = "Optional hint").hint)
    }

    @Test
    fun `showError true when passed true`() {
        assertTrue(scope(showError = true).showError)
    }

    @Test
    fun `showError false by default`() {
        assertFalse(scope().showError)
    }

    @Test
    fun `errorMessage stored when provided`() {
        assertEquals("Required", scope(showError = true, errorMessage = "Required").errorMessage)
    }

    @Test
    fun `errorMessage null when not provided`() {
        assertNull(scope().errorMessage)
    }
}
