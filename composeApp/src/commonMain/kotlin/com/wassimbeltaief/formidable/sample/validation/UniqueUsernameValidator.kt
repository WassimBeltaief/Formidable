package com.wassimbeltaief.formidable.sample.validation

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.AsyncFieldValidator
import kotlinx.coroutines.delay

class UniqueUsernameValidator : AsyncFieldValidator<String> {
    override suspend fun validate(value: String): ValidationResult {
        delay(500) // Simulate network call
        val taken = listOf("admin", "user", "test", "root")
        return if (value.lowercase() in taken) {
            ValidationResult.Invalid(listOf("Username '$value' is already taken"))
        } else {
            ValidationResult.Valid
        }
    }
}
