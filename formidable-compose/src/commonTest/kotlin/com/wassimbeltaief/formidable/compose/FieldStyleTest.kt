package com.wassimbeltaief.formidable.compose

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FieldStyleTest {
    @Test
    fun `Text enum has Outlined and Filled variants`() {
        val values = FieldStyle.Text.entries
        assertTrue(values.contains(FieldStyle.Text.Outlined))
        assertTrue(values.contains(FieldStyle.Text.Filled))
        assertEquals(2, values.size)
    }

    @Test
    fun `Toggle enum has four variants`() {
        val values = FieldStyle.Toggle.entries
        assertTrue(values.contains(FieldStyle.Toggle.Checkbox))
        assertTrue(values.contains(FieldStyle.Toggle.Switch))
        assertTrue(values.contains(FieldStyle.Toggle.CheckboxRow))
        assertTrue(values.contains(FieldStyle.Toggle.SwitchRow))
        assertEquals(4, values.size)
    }

    @Test
    fun `Picker enum has three variants`() {
        val values = FieldStyle.Picker.entries
        assertTrue(values.contains(FieldStyle.Picker.Dropdown))
        assertTrue(values.contains(FieldStyle.Picker.RadioGroup))
        assertTrue(values.contains(FieldStyle.Picker.SegmentedButton))
        assertEquals(3, values.size)
    }
}
