package com.wassimbeltaief.formforge.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import com.wassimbeltaief.formforge.core.state.FieldState

public class FormScope {

    private val focusRequesters = mutableListOf<FocusRequester>()

    internal fun resetFocusRequesters() = focusRequesters.clear()

    @Composable
    public fun Field(
        state: FieldState<String>,
        onValueChange: (String) -> Unit,
        onFocusLost: () -> Unit,
        content: @Composable FieldScope.() -> Unit,
    ) {
        val requester = remember { FocusRequester().also { focusRequesters += it } }
        val index = focusRequesters.indexOf(requester)
        val isLast = index == focusRequesters.size - 1

        FieldScope(
            value = state.value,
            onValueChange = onValueChange,
            onFocusLost = onFocusLost,
            label = state.label,
            hint = state.hint,
            showError = state.showError,
            errorMessage = state.errorMessage,
            modifier = Modifier
                .focusRequester(requester)
                .onFocusChanged { if (!it.isFocused) onFocusLost() },
            keyboardOptions = KeyboardOptions(
                imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequesters.getOrNull(index + 1)?.requestFocus() },
            ),
        ).content()
    }
}

@Composable
public fun FormForge(content: @Composable FormScope.() -> Unit) {
    val scope = remember { FormScope() }
    scope.resetFocusRequesters()
    scope.content()
}
