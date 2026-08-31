package com.wassimbeltaief.formidable.core.validators

import com.wassimbeltaief.formidable.core.validation.builtin.MatchFieldValidator
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MatchFieldValidatorTest {
    @Test
    fun `valid when values match`() {
        val validator = MatchFieldValidator("password", "Passwords do not match")
        val formData = mapOf("password" to "secret123")
        val result = validator.validate("secret123", formData)
        assertTrue(result.isValid)
    }

    @Test
    fun `invalid when values differ`() {
        val validator = MatchFieldValidator("password", "Passwords do not match")
        val formData = mapOf("password" to "secret123")
        val result = validator.validate("different", formData)
        assertFalse(result.isValid)
        assertTrue(result.errorsOrEmpty().contains("Passwords do not match"))
    }

    @Test
    fun `invalid when target field is null`() {
        val validator = MatchFieldValidator("password", "Passwords do not match")
        val formData = mapOf("password" to null)
        val result = validator.validate("secret123", formData)
        assertFalse(result.isValid)
    }

    @Test
    fun `valid when both are null`() {
        val validator = MatchFieldValidator("password", "Passwords do not match")
        val formData = mapOf("password" to null)
        val result = validator.validate(null, formData)
        assertTrue(result.isValid)
    }

    @Test
    fun `valid when both are empty strings`() {
        val validator = MatchFieldValidator("password", "Passwords do not match")
        val formData = mapOf("password" to "")
        val result = validator.validate("", formData)
        assertTrue(result.isValid)
    }
}
