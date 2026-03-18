package com.wassimbeltaief.formforge.core.validators

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.builtin.MinLengthValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MinLengthValidatorTest {

    private val validator = MinLengthValidator(min = 3, message = "Too short")

    @Test
    fun `value equal to min is valid`() {
        assertTrue(validator.validate("abc").isValid)
    }

    @Test
    fun `value longer than min is valid`() {
        assertTrue(validator.validate("abcde").isValid)
    }

    @Test
    fun `value shorter than min is invalid`() {
        val result = validator.validate("ab")
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Too short", (result as ValidationResult.Invalid).errors.first())
    }

    @Test
    fun `empty string is invalid`() {
        assertTrue(validator.validate("") is ValidationResult.Invalid)
    }

    @Test
    fun `whitespace-only counts toward length`() {
        // "   " has length 3, exactly meets min=3
        assertTrue(MinLengthValidator(3).validate("   ").isValid)
    }

    @Test
    fun `unicode characters counted by code unit length`() {
        // "é" is a single char — length 1, fails min=3
        assertTrue(MinLengthValidator(3).validate("é") is ValidationResult.Invalid)
        // 3 unicode chars passes min=3
        assertTrue(MinLengthValidator(3).validate("éàü").isValid)
    }
}
