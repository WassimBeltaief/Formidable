package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class RequiredIfValidator(
    private val targetField: String,
    private val targetValue: String,
    private val message: String = "This field is required",
) : FieldValidator<String?> {
    override fun validate(
        value: String?,
        formData: Map<String, Any?>,
    ): ValidationResult {
        val actualTargetValue = formData[targetField]?.toString() ?: ""
        if (actualTargetValue != targetValue) {
            return ValidationResult.Valid
        }
        return if (!value.isNullOrBlank()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(listOf(message))
        }
    }
}
