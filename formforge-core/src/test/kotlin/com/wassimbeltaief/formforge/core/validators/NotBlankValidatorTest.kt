package com.wassimbeltaief.formforge.core.validators

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.builtin.NotBlankValidator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NotBlankValidatorTest {

    private val validator = NotBlankValidator()

    @Test
    fun `non-blank value is valid`() {
        assertTrue(validator.validate("hello").isValid)
    }

    @Test
    fun `value with leading and trailing spaces is valid`() {
        assertTrue(validator.validate("  hello  ").isValid)
    }

    @Test
    fun `empty string is invalid`() {
        assertTrue(validator.validate("") is ValidationResult.Invalid)
    }

    @Test
    fun `whitespace-only string is invalid`() {
        assertTrue(validator.validate("   ") is ValidationResult.Invalid)
    }

    @Test
    fun `tab-only string is invalid`() {
        assertTrue(validator.validate("\t") is ValidationResult.Invalid)
    }

    @Test
    fun `newline-only string is invalid`() {
        assertTrue(validator.validate("\n") is ValidationResult.Invalid)
    }
}
