package com.wassimbeltaief.formidable.core.validators

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.builtin.RequiredIfValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequiredIfValidatorTest {
    @Test
    fun `valid when target field does not match target value`() {
        val validator = RequiredIfValidator("contactMethod", "email", "Email is required")
        val formData = mapOf("contactMethod" to "phone")
        assertTrue(validator.validate(null, formData).isValid)
        assertTrue(validator.validate("", formData).isValid)
    }

    @Test
    fun `valid when target field matches and value is provided`() {
        val validator = RequiredIfValidator("contactMethod", "email", "Email is required")
        val formData = mapOf("contactMethod" to "email")
        assertTrue(validator.validate("user@example.com", formData).isValid)
    }

    @Test
    fun `invalid when target field matches and value is null`() {
        val validator = RequiredIfValidator("contactMethod", "email", "Email is required")
        val formData = mapOf("contactMethod" to "email")
        val result = validator.validate(null, formData)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Email is required", (result as ValidationResult.Invalid).errors.first())
    }

    @Test
    fun `invalid when target field matches and value is blank`() {
        val validator = RequiredIfValidator("contactMethod", "email", "Email is required")
        val formData = mapOf("contactMethod" to "email")
        val result = validator.validate("", formData)
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun `invalid when target field matches and value is whitespace only`() {
        val validator = RequiredIfValidator("contactMethod", "email", "Email is required")
        val formData = mapOf("contactMethod" to "email")
        val result = validator.validate("   ", formData)
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun `valid when target field is missing from formData`() {
        val validator = RequiredIfValidator("contactMethod", "email", "Email is required")
        val formData = emptyMap<String, Any?>()
        assertTrue(validator.validate(null, formData).isValid)
    }

    @Test
    fun `works with boolean target value`() {
        val validator = RequiredIfValidator("shipToDifferent", "true", "Address is required")

        val formDataTrue = mapOf("shipToDifferent" to true)
        assertTrue(validator.validate(null, formDataTrue) is ValidationResult.Invalid)
        assertTrue(validator.validate("123 Main St", formDataTrue).isValid)

        val formDataFalse = mapOf("shipToDifferent" to false)
        assertTrue(validator.validate(null, formDataFalse).isValid)
    }
}
