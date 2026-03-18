package com.wassimbeltaief.formforge.core.state

public sealed class ValidationResult {
    public object Valid : ValidationResult()
    public data class Invalid(val errors: List<String>) : ValidationResult()
    public object Pending : ValidationResult()

    public val isValid: Boolean get() = this is Valid
    public val isPending: Boolean get() = this is Pending

    public fun errorsOrEmpty(): List<String> = if (this is Invalid) errors else emptyList()
}
