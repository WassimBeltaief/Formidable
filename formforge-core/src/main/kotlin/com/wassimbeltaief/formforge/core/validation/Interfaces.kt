package com.wassimbeltaief.formforge.core.validation

import com.wassimbeltaief.formforge.core.state.ValidationResult

public interface FieldValidator<T> {
    public fun validate(value: T): ValidationResult
}

public interface AsyncFieldValidator<T> {
    public suspend fun validate(value: T): ValidationResult
}
