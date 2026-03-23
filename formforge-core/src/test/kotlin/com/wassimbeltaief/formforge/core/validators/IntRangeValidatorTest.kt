package com.wassimbeltaief.formforge.core.validators

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.builtin.IntRangeValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IntRangeValidatorTest {

    @Test
    fun `value within range returns Valid`() {
        assertEquals(ValidationResult.Valid, IntRangeValidator(min = 18, max = 120).validate(25))
    }

    @Test
    fun `value at min boundary returns Valid`() {
        assertEquals(ValidationResult.Valid, IntRangeValidator(min = 18, max = 120).validate(18))
    }

    @Test
    fun `value at max boundary returns Valid`() {
        assertEquals(ValidationResult.Valid, IntRangeValidator(min = 18, max = 120).validate(120))
    }

    @Test
    fun `value below min returns Invalid`() {
        val result = IntRangeValidator(min = 18, max = 120, message = "Must be 18-120").validate(17)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Must be 18-120", (result as ValidationResult.Invalid).errors.first())
    }

    @Test
    fun `value above max returns Invalid`() {
        val result = IntRangeValidator(min = 18, max = 120, message = "Must be 18-120").validate(121)
        assertTrue(result is ValidationResult.Invalid)
    }

    @Test
    fun `only min constraint respected`() {
        assertEquals(ValidationResult.Valid, IntRangeValidator(min = 0).validate(1000))
        assertTrue(IntRangeValidator(min = 0).validate(-1) is ValidationResult.Invalid)
    }

    @Test
    fun `only max constraint respected`() {
        assertEquals(ValidationResult.Valid, IntRangeValidator(max = 100).validate(-50))
        assertTrue(IntRangeValidator(max = 100).validate(101) is ValidationResult.Invalid)
    }
}
