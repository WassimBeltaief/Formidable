package com.wassimbeltaief.formidable.core.validators

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.builtin.MaxLengthValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MaxLengthValidatorTest {

    @Test
    fun `valid when string length is under max`() {
        val validator = MaxLengthValidator(10)
        assertTrue(validator.validate("hello").isValid)
    }

    @Test
    fun `valid when string length equals max`() {
        val validator = MaxLengthValidator(5)
        assertTrue(validator.validate("hello").isValid)
    }

    @Test
    fun `invalid when string length exceeds max`() {
        val validator = MaxLengthValidator(3, "Too long")
        val result = validator.validate("hello")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Too long", (result as ValidationResult.Invalid).errors.first())
    }

    @Test
    fun `empty string is valid`() {
        val validator = MaxLengthValidator(5)
        assertTrue(validator.validate("").isValid)
    }
}