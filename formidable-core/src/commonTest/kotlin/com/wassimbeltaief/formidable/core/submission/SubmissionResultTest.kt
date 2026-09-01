package com.wassimbeltaief.formidable.core.submission

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame

class SubmissionResultTest {
    @Test
    fun `Success stores result value`() {
        val result = SubmissionResult.Success(42)
        assertEquals(42, result.result)
    }

    @Test
    fun `Success works with string type`() {
        val result = SubmissionResult.Success("ok")
        assertEquals("ok", result.result)
    }

    @Test
    fun `Error stores throwable`() {
        val error = RuntimeException("boom")
        val result = SubmissionResult.Error(error)
        assertSame(error, result.error)
        assertEquals("boom", result.error.message)
    }

    @Test
    fun `Cancelled is a singleton`() {
        assertSame(SubmissionResult.Cancelled, SubmissionResult.Cancelled)
    }

    @Test
    fun `Cancelled is a SubmissionResult`() {
        assertIs<SubmissionResult>(SubmissionResult.Cancelled)
    }

    @Test
    fun `when expression is exhaustive`() {
        val label =
            when (val r: SubmissionResult = SubmissionResult.Cancelled) {
                is SubmissionResult.Success<*> -> "success"
                is SubmissionResult.Error -> "error"
                SubmissionResult.Cancelled -> "cancelled"
            }
        assertEquals("cancelled", label)
    }

    @Test
    fun `Success result can be null`() {
        val result = SubmissionResult.Success<String?>(null)
        assertNull(result.result)
    }
}
