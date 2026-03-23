package com.wassimbeltaief.formforge.core.validation.builtin

import com.wassimbeltaief.formforge.core.state.ValidationResult
import com.wassimbeltaief.formforge.core.validation.FieldValidator

public class IntRangeValidator(
    private val min: Int? = null,
    private val max: Int? = null,
    private val message: String = "Value out of range",
) : FieldValidator<Int> {
    override fun validate(value: Int): ValidationResult {
        val belowMin = min != null && value < min
        val aboveMax = max != null && value > max
        return if (belowMin || aboveMax) ValidationResult.Invalid(listOf(message))
        else ValidationResult.Valid
    }
}
