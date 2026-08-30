package com.wassimbeltaief.formidable.ksp.parser

/** Supported field types for the current implementation slice. */
internal enum class FieldType {
    STRING,
    BOOLEAN,
    INT,
    ENUM,
}

/** A validator rule captured from an annotation on a property. */
internal sealed class ValidatorRule {
    abstract val order: Int

    data class NotBlank(override val order: Int, val message: String) : ValidatorRule()
    data class MinLength(override val order: Int, val min: Int, val message: String) : ValidatorRule()
    data class MaxLength(override val order: Int, val max: Int, val message: String) : ValidatorRule()
    data class Email(override val order: Int, val message: String) : ValidatorRule()
    data class Pattern(override val order: Int, val regex: String, val message: String) : ValidatorRule()
    data class RequiredIf(override val order: Int, val targetField: String, val targetValue: String, val message: String) : ValidatorRule()
    data class MatchField(override val order: Int, val targetField: String, val message: String) : ValidatorRule()
    data class Async(override val order: Int = Int.MAX_VALUE, val validatorFqn: String) : ValidatorRule()
    data class MustBeTrue(override val order: Int, val message: String) : ValidatorRule()
    data class IntRange(override val order: Int, val min: Int?, val max: Int?, val message: String) : ValidatorRule()
}

/** Visibility rule captured from @VisibleWhen annotation. */
internal data class VisibleWhenRule(
    val targetField: String,
    val targetValue: String,
)

/** Model for a single field in a @FormSchema class. */
internal data class FieldModel(
    val name: String,
    val type: FieldType,
    val isNullable: Boolean,
    val isOptional: Boolean,
    val label: String,
    val hint: String,
    val validators: List<ValidatorRule>,
    val visibleWhen: VisibleWhenRule? = null,
    val enumFqn: String? = null,
)

/** Model for an entire @FormSchema class. */
internal data class SchemaModel(
    val packageName: String,
    val schemaClassName: String,
    val fields: List<FieldModel>,
)
