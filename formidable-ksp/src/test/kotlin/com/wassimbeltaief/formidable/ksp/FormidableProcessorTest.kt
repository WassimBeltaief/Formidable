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

class FormidableProcessorTest {

    private fun compile(vararg sources: SourceFile): JvmCompilationResult {
        val compilation = KotlinCompilation().apply {
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
        $extraImports
        $body
        """.trimIndent(),
    )

    // --- String field ---

    @Test
    fun `generates controller for String field`() {
        val c = compile(formSource("LoginForm", body = """
            @FormSchema
            data class LoginForm(
                @Field(label = "Email")
                val email: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("LoginFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("class LoginFormController"))
        assertTrue(src.contains("val email"))
        assertTrue(src.contains("fun updateEmail"))
        assertTrue(src.contains("fun touchEmail"))
    }

    @Test
    fun `generated controller has reset, clear, validateAllSync, data, isValid`() {
        val c = compile(formSource("ResetForm", body = """
            @FormSchema
            data class ResetForm(
                @Field(label = "Name")
                val name: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("ResetFormController.kt")!!
        assertTrue(src.contains("fun reset"))
        assertTrue(src.contains("fun clear"))
        assertTrue(src.contains("fun validateAllSync"))
        assertTrue(src.contains("`data`") || src.contains("val data"))
        assertTrue(src.contains("isValid"))
    }

    // --- Boolean field ---

    @Test
    fun `generates controller for Boolean field`() {
        val c = compile(formSource("TermsForm", body = """
            @FormSchema
            data class TermsForm(
                @Field(label = "Accept Terms")
                val accepted: Boolean = false,
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("TermsFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("class TermsFormController"))
        assertTrue(src.contains("val accepted"))
        assertTrue(src.contains("fun updateAccepted"))
        assertTrue(src.contains("fun touchAccepted"))
    }

    // --- Int field ---

    @Test
    fun `generates controller for Int field`() {
        val c = compile(formSource("AgeForm", body = """
            @FormSchema
            data class AgeForm(
                @Field(label = "Age")
                val age: Int = 0,
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("AgeFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("class AgeFormController"))
        assertTrue(src.contains("val age"))
        assertTrue(src.contains("fun updateAge"))
    }

    // --- Nullable String field ---

    @Test
    fun `generates controller for nullable String field`() {
        val c = compile(formSource("ProfileForm", body = """
            @FormSchema
            data class ProfileForm(
                @Field(label = "Bio", optional = true)
                val bio: String? = null,
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("ProfileFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("class ProfileFormController"))
        assertTrue(src.contains("val bio"))
        assertTrue(src.contains("fun updateBio"))
    }

    // --- Enum field ---

    @Test
    fun `generates controller for enum field`() {
        val c = compile(formSource("RoleForm", body = """
            enum class Role { Admin, User, Guest }

            @FormSchema
            data class RoleForm(
                @Field(label = "Role")
                val role: Role = Role.User,
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("RoleFormController.kt")
        assertNotNull(src)
        assertTrue(src!!.contains("class RoleFormController"))
        assertTrue(src.contains("val role"))
        assertTrue(src.contains("fun updateRole"))
    }

    // --- Validators ---

    @Test
    fun `generates Email validator`() {
        val c = compile(formSource("EmailForm", body = """
            @FormSchema
            data class EmailForm(
                @Field(label = "Email")
                @Email
                val email: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("EmailFormController.kt")!!
        assertTrue(src.contains("email"))
        assertTrue(src.contains("EmailValidator") || src.contains("Email"))
    }

    @Test
    fun `generates MinLength validator`() {
        val c = compile(formSource("PasswordForm", body = """
            @FormSchema
            data class PasswordForm(
                @Field(label = "Password")
                @MinLength(8)
                val password: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("PasswordFormController.kt")!!
        assertTrue(src.contains("password"))
        assertTrue(src.contains("MinLength") || src.contains("minLength"))
    }

    @Test
    fun `generates MaxLength validator`() {
        val c = compile(formSource("BioForm", body = """
            @FormSchema
            data class BioForm(
                @Field(label = "Bio")
                @MaxLength(200)
                val bio: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("BioFormController.kt")!!
        assertTrue(src.contains("bio"))
        assertTrue(src.contains("MaxLength") || src.contains("maxLength"))
    }

    @Test
    fun `generates NotBlank validator`() {
        val c = compile(formSource("NameForm", body = """
            @FormSchema
            data class NameForm(
                @Field(label = "Name")
                @NotBlank
                val name: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("NameFormController.kt")!!
        assertTrue(src.contains("name"))
        assertTrue(src.contains("NotBlank") || src.contains("notBlank"))
    }

    @Test
    fun `generates MustBeTrue validator for Boolean field`() {
        val c = compile(formSource("ConsentForm", body = """
            @FormSchema
            data class ConsentForm(
                @Field(label = "I agree to the terms")
                @MustBeTrue
                val agreed: Boolean = false,
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("ConsentFormController.kt")!!
        assertTrue(src.contains("agreed"))
        assertTrue(src.contains("MustBeTrue") || src.contains("mustBeTrue"))
    }

    @Test
    fun `generates MatchField validator for cross-field password confirmation`() {
        val c = compile(formSource("SignUpForm", body = """
            @FormSchema
            data class SignUpForm(
                @Field(label = "Password")
                val password: String = "",

                @Field(label = "Confirm Password")
                @MatchField(targetField = "password")
                val confirmPassword: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("SignUpFormController.kt")!!
        assertTrue(src.contains("confirmPassword"))
        assertTrue(src.contains("password"))
        assertTrue(src.contains("MatchField") || src.contains("matchField"))
    }

    @Test
    fun `generates RequiredIf validator for conditional required field`() {
        val c = compile(formSource("ContactForm", body = """
            @FormSchema
            data class ContactForm(
                @Field(label = "Contact Method")
                val method: String = "",

                @Field(label = "Phone Number")
                @RequiredIf(targetField = "method", targetValue = "phone")
                val phone: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("ContactFormController.kt")!!
        assertTrue(src.contains("phone"))
        assertTrue(src.contains("method"))
        assertTrue(src.contains("RequiredIf") || src.contains("requiredIf"))
    }

    // --- Multiple fields ---

    @Test
    fun `generates controller for multiple mixed-type fields`() {
        val c = compile(formSource("FullForm", body = """
            @FormSchema
            data class FullForm(
                @Field(label = "Name")
                @NotBlank
                val name: String = "",

                @Field(label = "Age")
                val age: Int = 0,

                @Field(label = "Active")
                val active: Boolean = false,
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("FullFormController.kt")!!
        assertTrue(src.contains("val name"))
        assertTrue(src.contains("val age"))
        assertTrue(src.contains("val active"))
        assertTrue(src.contains("fun updateName"))
        assertTrue(src.contains("fun updateAge"))
        assertTrue(src.contains("fun updateActive"))
    }

    // --- Package ---

    @Test
    fun `generated controller uses the source class package`() {
        val c = compile(formSource("PkgForm", pkg = "com.test.myforms", body = """
            @FormSchema
            data class PkgForm(
                @Field(label = "Username")
                val username: String = "",
            )
        """.trimIndent()))
        assertEquals(KotlinCompilation.ExitCode.OK, c.exitCode, c.messages)
        val src = c.generatedSource("PkgFormController.kt")!!
        assertTrue(src.contains("package com.test.myforms"))
    }

    // --- Error case ---

    @Test
    fun `reports error when @FormSchema class has no supported fields`() {
        val c = compile(formSource("EmptyForm", body = """
            @FormSchema
            data class EmptyForm(
                val unsupported: List<String> = emptyList(),
            )
        """.trimIndent()))
        // KSP calls logger.error() → fails compilation
        assertTrue(
            c.exitCode != KotlinCompilation.ExitCode.OK || c.messages.contains("no supported fields"),
            "Expected failure for @FormSchema with no supported fields but got: ${c.exitCode}",
        )
    }
}
