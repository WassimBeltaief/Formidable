package com.wassimbeltaief.formforge.core.state

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FieldStateTest {

    @Test
    fun `isValid is true when errors are empty`() {
        val state = FieldState(value = "hello", initialValue = "hello")
        assertTrue(state.isValid)
    }

    @Test
    fun `isValid is false when errors are present`() {
        val state = FieldState(value = "", initialValue = "", errors = listOf("Required"))
        assertFalse(state.isValid)
    }

    @Test
    fun `showError is false when not touched even with errors`() {
        val state = FieldState(value = "", initialValue = "", errors = listOf("Required"), isTouched = false)
        assertFalse(state.showError)
    }

    @Test
    fun `showError is false when touched but no errors`() {
        val state = FieldState(value = "x", initialValue = "", errors = emptyList(), isTouched = true)
        assertFalse(state.showError)
    }

    @Test
    fun `showError is true when touched and has errors`() {
        val state = FieldState(value = "", initialValue = "", errors = listOf("Required"), isTouched = true)
        assertTrue(state.showError)
    }

    @Test
    fun `errorMessage returns null when errors are empty`() {
        val state = FieldState(value = "ok", initialValue = "ok")
        assertNull(state.errorMessage)
    }

    @Test
    fun `errorMessage returns first error`() {
        val state = FieldState(value = "", initialValue = "", errors = listOf("First error", "Second error"))
        assertEquals("First error", state.errorMessage)
    }

    @Test
    fun `isDirty reflects constructor value`() {
        val state = FieldState(value = "new", initialValue = "old", isDirty = true)
        assertTrue(state.isDirty)
    }
}
