package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class MatchFieldValidator(
    private val targetField: String,
    private val message: String = "Fields do not match",
) : FieldValidator<String?> {
    override fun validate(
        value: String?,
        formData: Map<String, Any?>,
    ): ValidationResult {
        val targetValue = formData[targetField]?.toString()
        return if (value == targetValue) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(listOf(message))
        }
    }
}
