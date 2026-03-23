package com.wassimbeltaief.formforge.compose

import androidx.compose.ui.Modifier

public class BooleanFieldScope internal constructor(
    public val checked: Boolean,
    public val onCheckedChange: (Boolean) -> Unit,
    public val label: String,
    public val hint: String,
    public val showError: Boolean,
    public val errorMessage: String?,
    public val modifier: Modifier,
)
