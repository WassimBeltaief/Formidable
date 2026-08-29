package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class MaxLengthValidator(
    private val max: Int,
    private val message: String = "Must be at most $max characters",
) : FieldValidator<String> {
    override fun validate(value: String): ValidationResult =
        if (value.length <= max) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))
}