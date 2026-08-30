package com.wassimbeltaief.formidable.ksp.parser

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType

private const val PKG = "com.wassimbeltaief.formidable.core.schema"

internal class SchemaParser(private val logger: KSPLogger) {

    fun parse(classDecl: KSClassDeclaration): SchemaModel? {
        val packageName = classDecl.packageName.asString()
        val className = classDecl.simpleName.asString()

        val allProps = classDecl.getAllProperties().toList()
        val fieldNames = allProps.mapNotNull { prop ->
            val resolved = prop.type.resolve()
            if (resolveFieldType(resolved) != null) prop.simpleName.asString() else null
        }.toSet()

        val fields = allProps.mapNotNull { parseField(it, fieldNames) }.toList()

        if (fields.isEmpty()) {
            logger.error("@FormSchema class $className has no supported fields", classDecl)
            return null
        }

        return SchemaModel(packageName, className, fields)
    }

    private fun parseField(prop: KSPropertyDeclaration, allFieldNames: Set<String>): FieldModel? {
        val resolvedType = prop.type.resolve()
        val type = resolveFieldType(resolvedType) ?: return null
        val isNullable = resolvedType.isMarkedNullable
        val name = prop.simpleName.asString()
        val annotations = prop.annotations.toList()

        val fieldAnnotation = annotations.findByFqn("$PKG.Field")
        val label = fieldAnnotation?.getArg("label") ?: ""
        val hint = fieldAnnotation?.getArg("hint") ?: ""
        val isOptional = fieldAnnotation?.getArg("optional") ?: false

        val visibleWhen = parseVisibleWhen(annotations, allFieldNames, prop)

        return FieldModel(
            name = name,
            type = type,
            isNullable = isNullable,
            isOptional = isOptional,
            label = label,
            hint = hint,
            validators = buildValidators(annotations, type, allFieldNames, prop),
            visibleWhen = visibleWhen,
        )
    }

    private fun parseVisibleWhen(
        annotations: List<KSAnnotation>,
        allFieldNames: Set<String>,
        prop: KSPropertyDeclaration,
    ): VisibleWhenRule? {
        val ann = annotations.findByFqn("$PKG.VisibleWhen") ?: return null
        val targetField = ann.getArg<String>("targetField") ?: ""
        val targetValue = ann.getArg<String>("targetValue") ?: ""

        if (targetField !in allFieldNames) {
            logger.error("@VisibleWhen targetField '$targetField' does not exist in this form", prop)
        }

        return VisibleWhenRule(targetField, targetValue)
    }

    private fun resolveFieldType(resolvedType: KSType): FieldType? {
        val typeName = resolvedType.declaration.qualifiedName?.asString()
        return when (typeName) {
            "kotlin.String" -> FieldType.STRING
            "kotlin.Boolean" -> FieldType.BOOLEAN
            "kotlin.Int" -> FieldType.INT
            else -> null
        }
    }

    private fun buildValidators(
        annotations: List<KSAnnotation>,
        type: FieldType,
        allFieldNames: Set<String>,
        prop: KSPropertyDeclaration,
    ): List<ValidatorRule> {
        val result = mutableListOf<ValidatorRule>()

        when (type) {
            FieldType.STRING -> {
                annotations.findByFqn("$PKG.NotBlank")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    result += ValidatorRule.NotBlank(order, it.getArg("message") ?: "Must not be blank")
                }
                annotations.findByFqn("$PKG.MinLength")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    val min = it.getArg<Int>("min") ?: 0
                    result += ValidatorRule.MinLength(order, min, it.getArg("message") ?: "Must be at least $min characters")
                }
                annotations.findByFqn("$PKG.MaxLength")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    val max = it.getArg<Int>("max") ?: Int.MAX_VALUE
                    result += ValidatorRule.MaxLength(order, max, it.getArg("message") ?: "Must be at most $max characters")
                }
                annotations.findByFqn("$PKG.Email")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    result += ValidatorRule.Email(order, it.getArg("message") ?: "Invalid email address")
                }
                annotations.findByFqn("$PKG.Pattern")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    val regex = it.getArg<String>("regex") ?: ".*"
                    result += ValidatorRule.Pattern(order, regex, it.getArg("message") ?: "Invalid format")
                }
                annotations.findByFqn("$PKG.RequiredIf")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    val targetField = it.getArg<String>("targetField") ?: ""
                    val targetValue = it.getArg<String>("targetValue") ?: ""
                    if (targetField !in allFieldNames) {
                        logger.error("@RequiredIf targetField '$targetField' does not exist in this form", prop)
                    }
                    result += ValidatorRule.RequiredIf(order, targetField, targetValue, it.getArg("message") ?: "This field is required")
                }
                annotations.findByFqn("$PKG.MatchField")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    val targetField = it.getArg<String>("targetField") ?: ""
                    if (targetField !in allFieldNames) {
                        logger.error("@MatchField targetField '$targetField' does not exist in this form", prop)
                    }
                    result += ValidatorRule.MatchField(order, targetField, it.getArg("message") ?: "Fields do not match")
                }
                annotations.findByFqn("$PKG.AsyncValidation")?.let { ann ->
                    val validatorType = ann.arguments
                        .firstOrNull { it.name?.asString() == "validator" }
                        ?.value as? KSType
                    validatorType?.declaration?.qualifiedName?.asString()?.let { fqn ->
                        result += ValidatorRule.Async(validatorFqn = fqn)
                    }
                }
            }
            FieldType.BOOLEAN -> {
                annotations.findByFqn("$PKG.MustBeTrue")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    result += ValidatorRule.MustBeTrue(order, it.getArg("message") ?: "Must be accepted")
                }
            }
            FieldType.INT -> {
                annotations.findByFqn("$PKG.IntRange")?.let {
                    val order = it.getArg<Int>("order") ?: 0
                    val min = it.getArg<Int>("min").takeIf { v -> v != Int.MIN_VALUE }
                    val max = it.getArg<Int>("max").takeIf { v -> v != Int.MAX_VALUE }
                    result += ValidatorRule.IntRange(order, min, max, it.getArg("message") ?: "Value out of range")
                }
            }
        }

        return result.sortedBy { it.order }
    }

    private fun List<KSAnnotation>.findByFqn(fqn: String): KSAnnotation? =
        firstOrNull { it.annotationType.resolve().declaration.qualifiedName?.asString() == fqn }

    @Suppress("UNCHECKED_CAST")
    private fun <T> KSAnnotation.getArg(name: String): T? =
        arguments.firstOrNull { it.name?.asString() == name }?.value as? T
}
