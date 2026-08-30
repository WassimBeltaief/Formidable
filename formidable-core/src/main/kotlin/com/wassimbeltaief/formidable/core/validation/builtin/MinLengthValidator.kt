package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class MinLengthValidator(
    private val min: Int,
    private val message: String = "Must be at least $min characters",
) : FieldValidator<String> {
    override fun validate(value: String, formData: Map<String, Any?>): ValidationResult =
        if (value.length >= min) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))
}
