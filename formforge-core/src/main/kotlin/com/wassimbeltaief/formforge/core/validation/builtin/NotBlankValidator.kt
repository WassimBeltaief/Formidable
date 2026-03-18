package com.wassimbeltaief.formforge.core.validation.builtin

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.FieldValidator

public class NotBlankValidator(
    private val message: String = "Must not be blank",
) : FieldValidator<String> {
    override fun validate(value: String): ValidationResult =
        if (value.isNotBlank()) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))
}
