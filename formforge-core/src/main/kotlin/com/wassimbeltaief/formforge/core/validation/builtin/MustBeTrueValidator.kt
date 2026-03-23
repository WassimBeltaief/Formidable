package com.wassimbeltaief.formforge.core.validation.builtin

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.FieldValidator

public class MustBeTrueValidator(
    private val message: String = "Must be accepted",
) : FieldValidator<Boolean> {
    override fun validate(value: Boolean): ValidationResult =
        if (value) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))
}
