package com.wassimbeltaief.formidable.compose

import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private enum class Size { Small, Medium, Large }

class NullableEnumFieldScopeTest {
    private fun scope(
        selected: Size? = null,
        options: List<Size> = Size.entries,
        showError: Boolean = false,
        errorMessage: String? = null,
        isOptional: Boolean = true,
    ) = NullableEnumFieldScope(
        selected = selected,
        options = options,
        onSelect = {},
        label = "Size",
        hint = "",
        isOptional = isOptional,
        showError = showError,
        errorMessage = errorMessage,
        modifier = Modifier,
    )

    @Test
    fun `selected is null when not provided`() {
        assertNull(scope().selected)
    }

    @Test
    fun `selected reflects non-null value`() {
        assertEquals(Size.Large, scope(selected = Size.Large).selected)
    }

    @Test
    fun `options list is stored`() {
        assertEquals(Size.entries, scope().options)
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
        assertEquals("Required", scope(errorMessage = "Required").errorMessage)
    }

    @Test
    fun `isOptional true by default in this scope`() {
        assertTrue(scope().isOptional)
    }
}
