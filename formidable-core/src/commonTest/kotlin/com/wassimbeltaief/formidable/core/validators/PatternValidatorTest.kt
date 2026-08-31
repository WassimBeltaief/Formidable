package com.wassimbeltaief.formidable.core.validators

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.builtin.PatternValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PatternValidatorTest {
    @Test
    fun `matching pattern passes`() {
        val validator = PatternValidator("^[A-Z]{3}$")
        assertTrue(validator.validate("ABC").isValid)
    }

    @Test
    fun `non-matching pattern fails`() {
        val validator = PatternValidator("^[A-Z]{3}$", "Must be 3 uppercase letters")
        val result = validator.validate("abc")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Must be 3 uppercase letters", (result as ValidationResult.Invalid).errors.first())
    }

    @Test
    fun `empty string is valid`() {
        val validator = PatternValidator("^[0-9]+$")
        assertTrue(validator.validate("").isValid)
    }

    @Test
    fun `phone number pattern`() {
        val validator = PatternValidator("^\\+?[0-9]{10,14}$")
        assertTrue(validator.validate("+1234567890").isValid)
        assertTrue(validator.validate("1234567890").isValid)
    }

    @Test
    fun `partial match fails`() {
        val validator = PatternValidator("^[0-9]+$")
        val result = validator.validate("123abc")
        assertTrue(result is ValidationResult.Invalid)
    }
}
