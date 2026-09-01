@file:OptIn(ExperimentalCompilerApi::class)

package com.wassimbeltaief.formidable.ksp

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.configureKsp
import com.tschuchort.compiletesting.sourcesGeneratedBySymbolProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SchemaParserAnnotationTest {
    private fun compile(vararg sources: SourceFile): JvmCompilationResult {
        val compilation =
            KotlinCompilation().apply {
                this.sources = sources.toList()
                inheritClassPath = true
                configureKsp(true) {
                    symbolProcessorProviders += FormidableProcessorProvider()
                }
            }
        return compilation.compile()
    }

    private fun JvmCompilationResult.generatedSource(name: String): String? =
        sourcesGeneratedBySymbolProcessor
            .filter { it.name == name }
            .firstOrNull()
            ?.readText()

    private fun formSource(
        className: String,
        pkg: String = "com.example",
        body: String,
        extraImports: String = "",
    ) = SourceFile.kotlin(
        "$className.kt",
        """
        package $pkg
        import com.wassimbeltaief.formidable.core.schema.FormSchema
        import com.wassimbeltaief.formidable.core.schema.Field
        import com.wassimbeltaief.formidable.core.schema.NotBlank
        import com.wassimbeltaief.formidable.core.schema.Email
        import com.wassimbeltaief.formidable.core.schema.MinLength
        import com.wassimbeltaief.formidable.core.schema.MaxLength
        import com.wassimbeltaief.formidable.core.schema.MustBeTrue
        import com.wassimbeltaief.formidable.core.schema.IntRange
        import com.wassimbeltaief.formidable.core.schema.MatchField
        import com.wassimbeltaief.formidable.core.schema.RequiredIf
        import com.wassimbeltaief.formidable.core.schema.Pattern
        import com.wassimbeltaief.formidable.core.schema.VisibleWhen
        import com.wassimbeltaief.formidable.core.schema.AsyncValidation
        $extraImports
        $body
        """.trimIndent(),
    )

    // --- @Pattern ---

    @Test
    fun `generates PatternValidator for @Pattern on String field`() {
        val c =
            compile(
                formSource(
                    "SlugForm",
                    body =
                        """
                        @FormSchema
                        data class SlugForm(
                            @Field(label = "Slug")
                            @Pattern(regex = "^[a-z0-9-]+${'$'}")
                            val slug: String = "",
                        )
                        """.trimIndent(),
                ),
            )
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("SlugFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("PatternValidator"), "Expected PatternValidator in generated source")
        assertTrue(src.contains("slug"), "Expected slug field in generated source")
    }

    // --- @IntRange ---

    @Test
    fun `generates IntRangeValidator for @IntRange on Int field`() {
        val c =
            compile(
                formSource(
                    "RatingForm",
                    body =
                        """
                        @FormSchema
                        data class RatingForm(
                            @Field(label = "Rating")
                            @IntRange(min = 1, max = 5)
                            val rating: Int = 1,
                        )
                        """.trimIndent(),
                ),
            )
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("RatingFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("IntRangeValidator"), "Expected IntRangeValidator in generated source")
        assertTrue(src.contains("rating"), "Expected rating field in generated source")
    }

    // --- @VisibleWhen ---

    @Test
    fun `generates isVisible logic for @VisibleWhen with valid target`() {
        val c =
            compile(
                formSource(
                    "ContactVisibilityForm",
                    body =
                        """
                        @FormSchema
                        data class ContactVisibilityForm(
                            @Field(label = "Contact Method")
                            val method: String = "",

                            @Field(label = "Phone Number")
                            @VisibleWhen(targetField = "method", targetValue = "phone")
                            val phone: String = "",
                        )
                        """.trimIndent(),
                ),
            )
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("ContactVisibilityFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("isVisible"), "Expected isVisible logic in generated source")
        assertTrue(src.contains("phone"), "Expected phone field in generated source")
    }

    // --- @VisibleWhen invalid target ---

    @Test
    fun `reports error when @VisibleWhen targetField does not exist`() {
        val c =
            compile(
                formSource(
                    "BadVisibilityForm",
                    body =
                        """
                        @FormSchema
                        data class BadVisibilityForm(
                            @Field(label = "Name")
                            @VisibleWhen(targetField = "nonexistent", targetValue = "yes")
                            val name: String = "",
                        )
                        """.trimIndent(),
                ),
            )
        assertTrue(
            c.exitCode != KotlinCompilation.ExitCode.OK || c.messages.contains("nonexistent"),
            "Expected error for invalid @VisibleWhen targetField but got: ${c.exitCode}",
        )
    }

    // --- @RequiredIf invalid target ---

    @Test
    fun `reports error when @RequiredIf targetField does not exist`() {
        val c =
            compile(
                formSource(
                    "BadRequiredIfForm",
                    body =
                        """
                        @FormSchema
                        data class BadRequiredIfForm(
                            @Field(label = "Phone")
                            @RequiredIf(targetField = "nonexistent", targetValue = "phone")
                            val phone: String = "",
                        )
                        """.trimIndent(),
                ),
            )
        assertTrue(
            c.exitCode != KotlinCompilation.ExitCode.OK || c.messages.contains("nonexistent"),
            "Expected error for invalid @RequiredIf targetField but got: ${c.exitCode}",
        )
    }

    // --- @MatchField invalid target ---

    @Test
    fun `reports error when @MatchField targetField does not exist`() {
        val c =
            compile(
                formSource(
                    "BadMatchForm",
                    body =
                        """
                        @FormSchema
                        data class BadMatchForm(
                            @Field(label = "Confirm Password")
                            @MatchField(targetField = "nonexistent")
                            val confirmPassword: String = "",
                        )
                        """.trimIndent(),
                ),
            )
        assertTrue(
            c.exitCode != KotlinCompilation.ExitCode.OK || c.messages.contains("nonexistent"),
            "Expected error for invalid @MatchField targetField but got: ${c.exitCode}",
        )
    }

    // --- @AsyncValidation ---

    @Test
    fun `generates async validation block for @AsyncValidation`() {
        val validatorSource =
            SourceFile.kotlin(
                "UsernameValidator.kt",
                """
                package com.example
                import com.wassimbeltaief.formidable.core.validation.AsyncFieldValidator
                import com.wassimbeltaief.formidable.core.state.ValidationResult

                class UsernameValidator : AsyncFieldValidator<String> {
                    override suspend fun validate(value: String): ValidationResult =
                        if (value.length >= 3) ValidationResult.Valid
                        else ValidationResult.Invalid(listOf("Too short"))
                }
                """.trimIndent(),
            )
        val c =
            compile(
                formSource(
                    "AsyncForm",
                    body =
                        """
                        @FormSchema
                        data class AsyncForm(
                            @Field(label = "Username")
                            @AsyncValidation(validator = UsernameValidator::class)
                            val username: String = "",
                        )
                        """.trimIndent(),
                    extraImports = "import com.example.UsernameValidator",
                ),
                validatorSource,
            )
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("AsyncFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("UsernameValidator"), "Expected UsernameValidator reference in generated source")
        assertTrue(src.contains("isValidating"), "Expected isValidating in async generated source")
    }
}
