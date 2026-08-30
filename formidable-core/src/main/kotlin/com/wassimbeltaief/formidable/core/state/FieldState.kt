package com.wassimbeltaief.formidable.core.state

public data class FieldState<T>(
    val value: T,
    val initialValue: T,
    val errors: List<String> = emptyList(),
    val isTouched: Boolean = false,
    val isDirty: Boolean = false,
    val isValidating: Boolean = false,
    val label: String = "",
    val hint: String = "",
    val isOptional: Boolean = false,
    val isVisible: Boolean = true,
) {
    public val isValid: Boolean get() = errors.isEmpty() && !isValidating
    public val showError: Boolean get() = isTouched && errors.isNotEmpty()
    public val errorMessage: String? get() = errors.firstOrNull()
}
