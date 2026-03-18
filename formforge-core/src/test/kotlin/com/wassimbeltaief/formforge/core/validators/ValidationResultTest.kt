package com.wassimbeltaief.formforge.core.validators

import com.wassimbeltaief.formforge.core.state.ValidationResult
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ValidationResultTest {

    @Test
    fun `Valid isValid is true`() {
        assertTrue(ValidationResult.Valid.isValid)
    }

    @Test
    fun `Valid isPending is false`() {
        assertFalse(ValidationResult.Valid.isPending)
    }

    @Test
    fun `Invalid isValid is false`() {
        assertFalse(ValidationResult.Invalid(listOf("error")).isValid)
    }

    @Test
    fun `Invalid isPending is false`() {
        assertFalse(ValidationResult.Invalid(listOf("error")).isPending)
    }

    @Test
    fun `Pending isPending is true`() {
        assertTrue(ValidationResult.Pending.isPending)
    }

    @Test
    fun `Pending isValid is false`() {
        assertFalse(ValidationResult.Pending.isValid)
    }

    @Test
    fun `errorsOrEmpty returns errors for Invalid`() {
        val errors = listOf("too short", "required")
        val result = ValidationResult.Invalid(errors).errorsOrEmpty()
        assertTrue(result == errors)
    }

    @Test
    fun `errorsOrEmpty returns empty list for Valid`() {
        assertTrue(ValidationResult.Valid.errorsOrEmpty().isEmpty())
    }

    @Test
    fun `errorsOrEmpty returns empty list for Pending`() {
        assertTrue(ValidationResult.Pending.errorsOrEmpty().isEmpty())
    }
}
