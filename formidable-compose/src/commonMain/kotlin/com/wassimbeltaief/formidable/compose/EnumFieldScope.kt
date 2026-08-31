package com.wassimbeltaief.formidable.compose

import androidx.compose.ui.Modifier

public class EnumFieldScope<T : Enum<T>> internal constructor(
    public val selected: T,
    public val options: List<T>,
    public val onSelect: (T) -> Unit,
    public val label: String,
    public val hint: String,
    public val isOptional: Boolean,
    public val showError: Boolean,
    public val errorMessage: String?,
    public val modifier: Modifier,
)
