package com.wassimbeltaief.formidable.core.state

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class FormStatusTest {
    @Test
    fun `Idle is a FormStatus`() {
        assertIs<FormStatus>(FormStatus.Idle)
    }

    @Test
    fun `Idle is a singleton`() {
        assertSame(FormStatus.Idle, FormStatus.Idle)
    }

    @Test
    fun `when expression is exhaustive over all variants`() {
        val result =
            when (val status: FormStatus = FormStatus.Idle) {
                FormStatus.Idle -> "idle"
            }
        kotlin.test.assertEquals("idle", result)
    }
}
