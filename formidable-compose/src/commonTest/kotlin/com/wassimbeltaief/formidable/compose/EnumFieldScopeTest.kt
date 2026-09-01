package com.wassimbeltaief.formidable.compose

import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private enum class Color { Red, Green, Blue }

class EnumFieldScopeTest {
    private fun scope(
        selected: Color = Color.Red,
        options: List<Color> = Color.entries,
        showError: Boolean = false,
        errorMessage: String? = null,
        isOptional: Boolean = false,
    ) = EnumFieldScope(
        selected = selected,
        options = options,
        onSelect = {},
        label = "Colour",
        hint = "",
        isOptional = isOptional,
        showError = showError,
        errorMessage = errorMessage,
        modifier = Modifier,
    )

    @Test
    fun `selected reflects provided value`() {
        assertEquals(Color.Green, scope(selected = Color.Green).selected)
    }

    @Test
    fun `options list is stored`() {
        assertEquals(3, scope().options.size)
        assertTrue(scope().options.contains(Color.Blue))
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
        assertEquals("Pick one", scope(showError = true, errorMessage = "Pick one").errorMessage)
    }

    @Test
    fun `errorMessage null by default`() {
        assertNull(scope().errorMessage)
    }

    @Test
    fun `isOptional false by default`() {
        assertFalse(scope().isOptional)
    }

    @Test
    fun `isOptional true when passed true`() {
        assertTrue(scope(isOptional = true).isOptional)
    }
}
