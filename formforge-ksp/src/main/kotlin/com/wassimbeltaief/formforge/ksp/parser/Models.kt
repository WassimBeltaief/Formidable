package com.wassimbeltaief.formforge.ksp.parser

/** Supported field types for the current implementation slice. */
internal enum class FieldType {
    STRING,
}

/** A validator rule captured from an annotation on a property. */
internal sealed class ValidatorRule {
    data class NotBlank(val message: String) : ValidatorRule()
    data class MinLength(val min: Int, val message: String) : ValidatorRule()
    data class Async(val validatorFqn: String) : ValidatorRule()
}

/** Model for a single field in a @FormSchema class. */
internal data class FieldModel(
    val name: String,
    val type: FieldType,
    val label: String,
    val hint: String,
    val validators: List<ValidatorRule>,
)

/** Model for an entire @FormSchema class. */
internal data class SchemaModel(
    val packageName: String,
    val schemaClassName: String,
    val fields: List<FieldModel>,
)
