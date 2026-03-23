package com.wassimbeltaief.formforge.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier

public class IntFieldScope internal constructor(
    public val value: Int,
    public val onValueChange: (Int) -> Unit,
    public val label: String,
    public val hint: String,
    public val showError: Boolean,
    public val errorMessage: String?,
    public val modifier: Modifier,
    public val keyboardOptions: KeyboardOptions,
    public val keyboardActions: KeyboardActions,
)
