package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class PatternValidator(
    regex: String,
    private val message: String = "Invalid format",
) : FieldValidator<String?> {
    private val pattern = Regex(regex)

    override fun validate(value: String?, formData: Map<String, Any?>): ValidationResult =
        if (value.isNullOrEmpty() || pattern.matches(value)) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))
}
