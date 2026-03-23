package com.wassimbeltaief.formforge.core.validators

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.builtin.MustBeTrueValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MustBeTrueValidatorTest {

    @Test
    fun `true returns Valid`() {
        assertEquals(ValidationResult.Valid, MustBeTrueValidator().validate(true))
    }

    @Test
    fun `false returns Invalid with default message`() {
        val result = MustBeTrueValidator().validate(false)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Must be accepted", (result as ValidationResult.Invalid).errors.first())
    }

    @Test
    fun `false returns Invalid with custom message`() {
        val result = MustBeTrueValidator("You must agree").validate(false)
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("You must agree", (result as ValidationResult.Invalid).errors.first())
    }
}
