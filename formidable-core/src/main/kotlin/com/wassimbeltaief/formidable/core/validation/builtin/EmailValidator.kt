package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

public class EmailValidator(
    private val message: String = "Invalid email address",
) : FieldValidator<String> {
    override fun validate(value: String): ValidationResult =
        if (value.isEmpty() || EMAIL_REGEX.matches(value)) ValidationResult.Valid
        else ValidationResult.Invalid(listOf(message))

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    }
}