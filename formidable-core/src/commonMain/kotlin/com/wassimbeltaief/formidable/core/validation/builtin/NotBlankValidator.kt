package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class NotBlankValidator(
    private val message: String = "Must not be blank",
) : FieldValidator<String> {
    override fun validate(
        value: String,
        formData: Map<String, Any?>,
    ): ValidationResult =
        if (value.isNotBlank()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(listOf(message))
        }
}
