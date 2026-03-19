package com.wassimbeltaief.formforge.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import com.wassimbeltaief.formforge.core.state.FieldState

public class FormScope internal constructor() {

    internal val focusRequesters = LinkedHashMap<Any, FocusRequester>()

    @Composable
    public fun Field(
        state: FieldState<String>,
        onValueChange: (String) -> Unit,
        onFocusLost: () -> Unit,
        content: @Composable FieldScope.() -> Unit,
    ) {
        val key = remember { Any() }
        val requester = remember { FocusRequester() }
        var imeAction by remember { mutableStateOf(ImeAction.Done) }
        var wasFocused by remember { mutableStateOf(false) }

        SideEffect {
            focusRequesters[key] = requester
            val values = focusRequesters.values.toList()
            val index = values.indexOf(requester)
            imeAction = if (index == values.size - 1) ImeAction.Done else ImeAction.Next
        }

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
                .onFocusChanged {
                    if (it.isFocused) wasFocused = true
                    else if (wasFocused) onFocusLost()
                },
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = {
                    val values = focusRequesters.values.toList()
                    val index = values.indexOf(requester)
                    values.getOrNull(index + 1)?.requestFocus()
                },
            ),
        ).content()
    }
}

@Composable
public fun FormForge(content: @Composable FormScope.() -> Unit) {
    val scope = remember { FormScope() }
    scope.focusRequesters.clear()
    scope.content()
}
