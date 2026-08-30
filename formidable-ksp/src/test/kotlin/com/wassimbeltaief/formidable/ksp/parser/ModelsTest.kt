package com.wassimbeltaief.formidable.ksp.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ModelsTest {
    @Test
    fun `FieldModel stores all properties correctly`() {
        val field =
            FieldModel(
                name = "email",
                type = FieldType.STRING,
                isNullable = false,
                isOptional = false,
                label = "Email address",
                hint = "you@example.com",
                validators = listOf(ValidatorRule.NotBlank(0, "Required")),
            )

        assertEquals("email", field.name)
        assertEquals(FieldType.STRING, field.type)
        assertEquals(false, field.isNullable)
        assertEquals(false, field.isOptional)
        assertEquals("Email address", field.label)
        assertEquals("you@example.com", field.hint)
        assertEquals(1, field.validators.size)
    }

    @Test
    fun `FieldModel supports nullable and optional flags`() {
        val field =
            FieldModel(
                name = "nickname",
                type = FieldType.STRING,
                isNullable = true,
                isOptional = true,
                label = "Nickname",
                hint = "Optional",
                validators = emptyList(),
            )

        assertEquals(true, field.isNullable)
        assertEquals(true, field.isOptional)
    }

    @Test
    fun `SchemaModel stores package, class name, and fields`() {
        val field = FieldModel("name", FieldType.STRING, false, false, "Name", "", emptyList())
        val schema = SchemaModel("com.example", "LoginForm", listOf(field))

        assertEquals("com.example", schema.packageName)
        assertEquals("LoginForm", schema.schemaClassName)
        assertEquals(1, schema.fields.size)
    }

    @Test
    fun `ValidatorRule subtypes hold their data`() {
        val notBlank = ValidatorRule.NotBlank(0, "Required")
        val minLength = ValidatorRule.MinLength(0, 8, "Too short")
        val async = ValidatorRule.Async(validatorFqn = "com.example.UniqueEmailValidator")

        assertEquals("Required", notBlank.message)
        assertEquals(8, minLength.min)
        assertEquals("com.example.UniqueEmailValidator", async.validatorFqn)
    }

    @Test
    fun `ValidatorRule order is used for sorting`() {
        val v1 = ValidatorRule.Email(2, "Invalid")
        val v2 = ValidatorRule.RequiredIf(1, "field", "value", "Required")
        val v3 = ValidatorRule.NotBlank(0, "Blank")

        val sorted = listOf(v1, v2, v3).sortedBy { it.order }

        assertEquals(0, sorted[0].order)
        assertEquals(1, sorted[1].order)
        assertEquals(2, sorted[2].order)
    }
}
