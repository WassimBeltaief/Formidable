package com.wassimbeltaief.formidable.core.validation.builtin

import com.wassimbeltaief.formidable.core.state.ValidationResult
import com.wassimbeltaief.formidable.core.validation.FieldValidator

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
