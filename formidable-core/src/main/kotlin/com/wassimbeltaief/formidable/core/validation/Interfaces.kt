package com.wassimbeltaief.formidable.core.validation

import com.wassimbeltaief.formidable.core.state.ValidationResult

public interface FieldValidator<T> {
    public fun validate(value: T): ValidationResult
}

public interface AsyncFieldValidator<T> {
    public suspend fun validate(value: T): ValidationResult
}
