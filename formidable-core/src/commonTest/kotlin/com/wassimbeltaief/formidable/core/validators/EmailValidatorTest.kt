package com.wassimbeltaief.formidable.core.validators

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.builtin.EmailValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmailValidatorTest {
    private val validator = EmailValidator()

    @Test
    fun `valid email passes`() {
        assertTrue(validator.validate("user@example.com").isValid)
    }

    @Test
    fun `valid email with subdomain passes`() {
        assertTrue(validator.validate("user@mail.example.com").isValid)
    }

    @Test
    fun `valid email with plus sign passes`() {
        assertTrue(validator.validate("user+tag@example.com").isValid)
    }

    @Test
    fun `empty string is valid`() {
        assertTrue(validator.validate("").isValid)
    }

    @Test
    fun `missing at sign is invalid`() {
        val result = validator.validate("userexample.com")
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun `missing domain is invalid`() {
        val result = validator.validate("user@")
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun `missing tld is invalid`() {
        val result = validator.validate("user@example")
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun `custom message is used`() {
        val validator = EmailValidator("Please enter a valid email")
        val result = validator.validate("invalid")
        assertEquals("Please enter a valid email", (result as ValidationResult.Invalid).errors.first())
    }
}
