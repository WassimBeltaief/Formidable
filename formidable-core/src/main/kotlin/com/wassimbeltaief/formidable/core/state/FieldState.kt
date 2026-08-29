package com.wassimbeltaief.formidable.core.state

public data class FieldState<T>(
    val value: T,
    val initialValue: T,
    val errors: List<String> = emptyList(),
    val isTouched: Boolean = false,
    val isDirty: Boolean = false,
    val label: String = "",
    val hint: String = "",
    val isOptional: Boolean = false,
) {
    public val isValid: Boolean get() = errors.isEmpty()
    public val showError: Boolean get() = isTouched && errors.isNotEmpty()
    public val errorMessage: String? get() = errors.firstOrNull()
}
