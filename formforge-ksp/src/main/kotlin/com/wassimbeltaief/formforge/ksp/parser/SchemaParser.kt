package com.wassimbeltaief.formforge.ksp.parser

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType

private const val PKG = "com.wassimbeltaief.formforge.core.schema"

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
        val type = resolveFieldType(prop) ?: return null
        val name = prop.simpleName.asString()
        val annotations = prop.annotations.toList()

        val fieldAnnotation = annotations.findByFqn("$PKG.Field")
        val label = fieldAnnotation?.getArg("label") ?: ""
        val hint = fieldAnnotation?.getArg("hint") ?: ""

        return FieldModel(
            name = name,
            type = type,
            label = label,
            hint = hint,
            validators = buildValidators(annotations, type),
        )
    }

    private fun resolveFieldType(prop: KSPropertyDeclaration): FieldType? {
        val typeName = prop.type.resolve().declaration.qualifiedName?.asString()
        return when (typeName) {
            "kotlin.String" -> FieldType.STRING
            else -> null
        }
    }

    private fun buildValidators(
        annotations: List<KSAnnotation>,
        type: FieldType,
    ): List<ValidatorRule> {
        if (type != FieldType.STRING) return emptyList()
        val result = mutableListOf<ValidatorRule>()

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

        return result
    }

    private fun List<KSAnnotation>.findByFqn(fqn: String): KSAnnotation? =
        firstOrNull { it.annotationType.resolve().declaration.qualifiedName?.asString() == fqn }

    @Suppress("UNCHECKED_CAST")
    private fun <T> KSAnnotation.getArg(name: String): T? =
        arguments.firstOrNull { it.name?.asString() == name }?.value as? T
}
