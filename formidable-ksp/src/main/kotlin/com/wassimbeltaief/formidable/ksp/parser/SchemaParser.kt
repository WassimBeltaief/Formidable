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

        val fields = classDecl.getAllProperties().mapNotNull { parseField(it) }.toList()

        if (fields.isEmpty()) {
            logger.error("@FormSchema class $className has no supported fields", classDecl)
            return null
        }

        return SchemaModel(packageName, className, fields)
    }

    private fun parseField(prop: KSPropertyDeclaration): FieldModel? {
        val resolvedType = prop.type.resolve()
        val type = resolveFieldType(resolvedType) ?: return null
        val isNullable = resolvedType.isMarkedNullable
        val name = prop.simpleName.asString()
        val annotations = prop.annotations.toList()

        val fieldAnnotation = annotations.findByFqn("$PKG.Field")
        val label = fieldAnnotation?.getArg("label") ?: ""
        val hint = fieldAnnotation?.getArg("hint") ?: ""
        val isOptional = fieldAnnotation?.getArg("optional") ?: false

        return FieldModel(
            name = name,
            type = type,
            isNullable = isNullable,
            isOptional = isOptional,
            label = label,
            hint = hint,
            validators = buildValidators(annotations, type),
        )
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
    ): List<ValidatorRule> {
        val result = mutableListOf<ValidatorRule>()

        when (type) {
            FieldType.STRING -> {
                annotations.findByFqn("$PKG.NotBlank")?.let {
                    result += ValidatorRule.NotBlank(it.getArg("message") ?: "Must not be blank")
                }
                annotations.findByFqn("$PKG.MinLength")?.let {
                    val min = it.getArg<Int>("min") ?: 0
                    result += ValidatorRule.MinLength(min, it.getArg("message") ?: "Must be at least $min characters")
                }
                annotations.findByFqn("$PKG.AsyncValidation")?.let { ann ->
                    val validatorType = ann.arguments
                        .firstOrNull { it.name?.asString() == "validator" }
                        ?.value as? KSType
                    validatorType?.declaration?.qualifiedName?.asString()?.let { fqn ->
                        result += ValidatorRule.Async(fqn)
                    }
                }
            }
            FieldType.BOOLEAN -> {
                annotations.findByFqn("$PKG.MustBeTrue")?.let {
                    result += ValidatorRule.MustBeTrue(it.getArg("message") ?: "Must be accepted")
                }
            }
            FieldType.INT -> {
                annotations.findByFqn("$PKG.IntRange")?.let {
                    val min = it.getArg<Int>("min").takeIf { v -> v != Int.MIN_VALUE }
                    val max = it.getArg<Int>("max").takeIf { v -> v != Int.MAX_VALUE }
                    result += ValidatorRule.IntRange(min, max, it.getArg("message") ?: "Value out of range")
                }
            }
        }

        return result
    }

    private fun List<KSAnnotation>.findByFqn(fqn: String): KSAnnotation? =
        firstOrNull { it.annotationType.resolve().declaration.qualifiedName?.asString() == fqn }

    @Suppress("UNCHECKED_CAST")
    private fun <T> KSAnnotation.getArg(name: String): T? =
        arguments.firstOrNull { it.name?.asString() == name }?.value as? T
}
