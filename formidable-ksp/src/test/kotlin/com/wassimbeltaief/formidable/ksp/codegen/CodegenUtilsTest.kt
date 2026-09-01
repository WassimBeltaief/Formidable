package com.wassimbeltaief.formidable.ksp.codegen

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CodegenUtilsTest {
    @Test
    fun `capitalize converts first char to uppercase`() {
        assertEquals("Username", "username".capitalize())
    }

    @Test
    fun `capitalize single lowercase char`() {
        assertEquals("A", "a".capitalize())
    }

    @Test
    fun `capitalize empty string returns empty`() {
        assertEquals("", "".capitalize())
    }

    @Test
    fun `capitalize already-capitalised string is unchanged`() {
        assertEquals("Hello", "Hello".capitalize())
    }

    @Test
    fun `capitalize all-uppercase string keeps first char, rest unchanged`() {
        assertEquals("USERNAME", "uSERNAME".capitalize())
    }
}
