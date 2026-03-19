package com.wassimbeltaief.formforge.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wassimbeltaief.formforge.core.state.FieldState

@Composable
public fun Field(
    state: FieldState<String>,
    onValueChange: (String) -> Unit,
    onFocusLost: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    content: @Composable FieldScope.() -> Unit,
) {
    val scope = FieldScope(
        value = state.value,
        onValueChange = onValueChange,
        onFocusLost = onFocusLost,
        label = state.label,
        hint = state.hint,
        showError = state.showError,
        errorMessage = state.errorMessage,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
    scope.content()
}
