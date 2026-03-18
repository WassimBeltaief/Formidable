package com.wassimbeltaief.formforge.ksp.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ModelsTest {

    @Test
    fun `FieldModel stores all properties correctly`() {
        val field = FieldModel(
            name = "email",
            type = FieldType.STRING,
            label = "Email address",
            hint = "you@example.com",
            validators = listOf(ValidatorRule.NotBlank("Required")),
        )

        assertEquals("email", field.name)
        assertEquals(FieldType.STRING, field.type)
        assertEquals("Email address", field.label)
        assertEquals("you@example.com", field.hint)
        assertEquals(1, field.validators.size)
    }

    @Test
    fun `SchemaModel stores package, class name, and fields`() {
        val field = FieldModel("name", FieldType.STRING, "Name", "", emptyList())
        val schema = SchemaModel("com.example", "LoginForm", listOf(field))

        assertEquals("com.example", schema.packageName)
        assertEquals("LoginForm", schema.schemaClassName)
        assertEquals(1, schema.fields.size)
    }

    @Test
    fun `ValidatorRule subtypes hold their data`() {
        val notBlank = ValidatorRule.NotBlank("Required")
        val minLength = ValidatorRule.MinLength(8, "Too short")
        val async = ValidatorRule.Async("com.example.UniqueEmailValidator")

        assertEquals("Required", notBlank.message)
        assertEquals(8, minLength.min)
        assertEquals("com.example.UniqueEmailValidator", async.validatorFqn)
    }
}
