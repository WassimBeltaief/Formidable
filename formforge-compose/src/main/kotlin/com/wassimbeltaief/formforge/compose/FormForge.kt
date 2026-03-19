package com.wassimbeltaief.formforge.compose

import androidx.compose.runtime.Composable

public class FormScope

@Composable
public fun FormForge(content: @Composable FormScope.() -> Unit) {
    FormScope().content()
}
