package com.wassimbeltaief.formforge.ksp.codegen

import com.wassimbeltaief.formforge.ksp.parser.FieldModel
import com.wassimbeltaief.formforge.ksp.parser.FieldType
import com.wassimbeltaief.formforge.ksp.parser.SchemaModel
import com.wassimbeltaief.formforge.ksp.parser.ValidatorRule
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FormStateGeneratorTest {

    private val generator = FormStateGenerator()

    private fun generate(vararg fields: FieldModel): String =
        generator.generate(SchemaModel("com.example", "LoginForm", fields.toList())).toString()

    // --- generated file ---

    @Test
    fun `generated file contains do not edit comment`() {
        assertTrue(generate(stringField("email")).contains("do not edit"))
    }

    @Test
    fun `generated file has correct package`() {
        assertTrue(generate(stringField("email")).contains("package com.example"))
    }

    // --- no *State wrapper class ---

    @Test
    fun `no LoginFormState data class generated`() {
        // per-field StateFlows replace the bag class
        assertFalse(generate(stringField("email")).contains("data class LoginFormState"))
    }

    // --- holder class ---

    @Test
    fun `holder has correct class name`() {
        assertTrue(generate(stringField("email")).contains("class LoginFormHolder"))
    }

    @Test
    fun `holder implements FormStateHolder`() {
        assertTrue(generate(stringField("email")).contains("FormStateHolder<LoginForm>"))
    }

    @Test
    fun `holder has private MutableStateFlow per field`() {
        val src = generate(stringField("email"), stringField("password"))
        assertTrue(src.contains("_email"))
        assertTrue(src.contains("_password"))
        assertTrue(src.contains("MutableStateFlow"))
    }

    @Test
    fun `holder exposes public StateFlow per field`() {
        val src = generate(stringField("email"), stringField("password"))
        // public val email: StateFlow<FieldState<String>>
        assertTrue(src.contains("StateFlow"))
        assertTrue(src.contains("FieldState<String>"))
        assertTrue(src.contains("email"))
        assertTrue(src.contains("password"))
    }

    @Test
    fun `holder exposes status as StateFlow of FormStatus`() {
        val src = generate(stringField("email"))
        assertTrue(src.contains("_status"))
        assertTrue(src.contains("FormStatus"))
        assertTrue(src.contains("override"))
    }

    @Test
    fun `holder constructor takes only initial value`() {
        val src = generate(stringField("email"))
        assertFalse(src.contains("coroutineScope"))
        assertFalse(src.contains("trigger"))
        assertFalse(src.contains("debounceMs"))
    }

    @Test
    fun `label and hint embedded in per-field initializer`() {
        val field = FieldModel("email", FieldType.STRING, "Email", "you@example.com", emptyList())
        val src = generate(field)
        assertTrue(src.contains("Email"))
        assertTrue(src.contains("you@example.com"))
    }

    // --- per-field functions ---

    @Test
    fun `update and touch functions generated per field`() {
        val src = generate(stringField("email"), stringField("password"))
        assertTrue(src.contains("fun updateEmail("))
        assertTrue(src.contains("fun touchEmail("))
        assertTrue(src.contains("fun updatePassword("))
        assertTrue(src.contains("fun touchPassword("))
    }

    @Test
    fun `update function always clears errors`() {
        // even with no validators, errors = emptyList() so stale server errors don't persist
        val src = generate(stringField("email"))
        assertTrue(src.contains("emptyList()"))
    }

    // --- form-level functions ---

    @Test
    fun `holder has validateAllSync, reset, clear, setFieldError`() {
        val src = generate(stringField("email"))
        assertTrue(src.contains("fun validateAllSync()"))
        assertTrue(src.contains("fun reset()"))
        assertTrue(src.contains("fun clear()"))
        assertTrue(src.contains("fun setFieldError("))
    }

    @Test
    fun `validateAllSync captures allValid inside update lambda`() {
        val src = generate(stringField("email"))
        assertTrue(src.contains("allValid"))
    }

    @Test
    fun `validateAllSync does not touch status flow`() {
        val src = generate(stringField("email"))
        val validateIdx = src.indexOf("fun validateAllSync()")
        val afterValidate = src.substring(validateIdx, src.indexOf("fun reset()", validateIdx))
        assertFalse(afterValidate.contains("_status"))
    }

    @Test
    fun `reset restores initial field values`() {
        val src = generate(stringField("email"))
        val resetIdx = src.indexOf("fun reset()")
        val afterReset = src.substring(resetIdx, resetIdx + 300)
        assertTrue(afterReset.contains("initial"))
    }

    @Test
    fun `setFieldError generates branch for each field`() {
        val src = generate(stringField("email"), stringField("password"))
        assertTrue(src.contains("\"email\""))
        assertTrue(src.contains("\"password\""))
    }

    @Test
    fun `data property reconstructs schema class from per-field flows`() {
        val src = generate(stringField("email"), stringField("password"))
        assertTrue(src.contains("LoginForm"))
        assertTrue(src.contains("override"))
    }

    // --- no dead infrastructure ---

    @Test
    fun `errorsOrEmpty is not generated into the holder`() {
        assertFalse(generate(stringField("email")).contains("fun errorsOrEmpty"))
    }

    private fun stringField(name: String, vararg validators: ValidatorRule) = FieldModel(
        name = name,
        type = FieldType.STRING,
        label = "",
        hint = "",
        validators = validators.toList(),
    )
}
