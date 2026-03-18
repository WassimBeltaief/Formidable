package com.wassimbeltaief.formforge.core.validation.builtin

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.FieldValidator

public class MinLengthValidator(
    private val min: Int,
    private val message: String = "Must be at least $min characters",
) : FieldValidator<String> {
    override fun validate(value: String): ValidationResult =
        if (value.length >= min) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))
}
