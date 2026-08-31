@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.wassimbeltaief.formidable.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.wassimbeltaief.formidable.core.state.FieldState

public class StringFieldConfig {
    public var style: FieldStyle.Text = FieldStyle.Text.Outlined
    public var keyboardType: KeyboardType = KeyboardType.Text
    public var supportingText: @Composable (() -> Unit)? = null
    public var trailingIcon: @Composable (() -> Unit)? = null
    public var leadingIcon: @Composable (() -> Unit)? = null
    public var visualTransformation: VisualTransformation = VisualTransformation.None
    public var singleLine: Boolean = true
}

public class NullableStringFieldConfig {
    public var style: FieldStyle.Text = FieldStyle.Text.Outlined
    public var keyboardType: KeyboardType = KeyboardType.Text
    public var supportingText: @Composable (() -> Unit)? = null
    public var trailingIcon: @Composable (() -> Unit)? = null
    public var leadingIcon: @Composable (() -> Unit)? = null
    public var visualTransformation: VisualTransformation = VisualTransformation.None
    public var singleLine: Boolean = true
}

public class IntFieldConfig {
    public var style: FieldStyle.Text = FieldStyle.Text.Outlined
    public var supportingText: @Composable (() -> Unit)? = null
    public var trailingIcon: @Composable (() -> Unit)? = null
    public var leadingIcon: @Composable (() -> Unit)? = null
    public var singleLine: Boolean = true
}

public class BooleanFieldConfig {
    public var style: FieldStyle.Toggle = FieldStyle.Toggle.CheckboxRow
}

public class EnumFieldConfig<T : Enum<T>> {
    public var style: FieldStyle.Picker = FieldStyle.Picker.Dropdown
    public var optionLabel: (T) -> String = {
        it.name.lowercase().replaceFirstChar { c -> c.uppercase() }
    }
}

@Composable
public fun FormScope.StringField(
    state: FieldState<String>,
    onValueChange: (String) -> Unit,
    onFocusLost: () -> Unit,
    config: StringFieldConfig.() -> Unit = {},
) {
    val c = StringFieldConfig().apply(config)
    StringField(state, onValueChange, onFocusLost) {
        val resolvedLabel = if (isOptional) "$label (optional)" else label
        val resolvedSupporting: @Composable (() -> Unit)? =
            c.supportingText ?: when {
                isValidating -> {
                    { CircularProgressIndicator(Modifier.size(20.dp)) }
                }
                showError -> {
                    { Text(errorMessage ?: "") }
                }
                else -> null
            }
        val resolvedTrailing: @Composable (() -> Unit)? =
            c.trailingIcon ?: when {
                isValidating -> {
                    { CircularProgressIndicator(Modifier.size(20.dp)) }
                }
                else -> null
            }
        when (c.style) {
            FieldStyle.Text.Outlined ->
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(resolvedLabel) },
                    placeholder =
                        if (hint.isNotEmpty()) {
                            { Text(hint) }
                        } else {
                            null
                        },
                    isError = showError,
                    supportingText = resolvedSupporting,
                    trailingIcon = resolvedTrailing,
                    leadingIcon = c.leadingIcon,
                    visualTransformation = c.visualTransformation,
                    keyboardOptions = keyboardOptions.copy(keyboardType = c.keyboardType),
                    keyboardActions = keyboardActions,
                    singleLine = c.singleLine,
                )
            FieldStyle.Text.Filled ->
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(resolvedLabel) },
                    placeholder =
                        if (hint.isNotEmpty()) {
                            { Text(hint) }
                        } else {
                            null
                        },
                    isError = showError,
                    supportingText = resolvedSupporting,
                    trailingIcon = resolvedTrailing,
                    leadingIcon = c.leadingIcon,
                    visualTransformation = c.visualTransformation,
                    keyboardOptions = keyboardOptions.copy(keyboardType = c.keyboardType),
                    keyboardActions = keyboardActions,
                    singleLine = c.singleLine,
                )
        }
    }
}

@Composable
public fun FormScope.NullableStringField(
    state: FieldState<String?>,
    onValueChange: (String?) -> Unit,
    onFocusLost: () -> Unit,
    config: NullableStringFieldConfig.() -> Unit = {},
) {
    val c = NullableStringFieldConfig().apply(config)
    NullableStringField(state, onValueChange, onFocusLost) {
        val resolvedLabel = if (isOptional) "$label (optional)" else label
        val resolvedSupporting: @Composable (() -> Unit)? =
            c.supportingText ?: when {
                isValidating -> {
                    { Text("Checking…") }
                }
                showError -> {
                    { Text(errorMessage ?: "") }
                }
                else -> null
            }
        when (c.style) {
            FieldStyle.Text.Outlined ->
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(resolvedLabel) },
                    placeholder =
                        if (hint.isNotEmpty()) {
                            { Text(hint) }
                        } else {
                            null
                        },
                    isError = showError,
                    supportingText = resolvedSupporting,
                    trailingIcon = c.trailingIcon,
                    leadingIcon = c.leadingIcon,
                    visualTransformation = c.visualTransformation,
                    keyboardOptions = keyboardOptions.copy(keyboardType = c.keyboardType),
                    keyboardActions = keyboardActions,
                    singleLine = c.singleLine,
                )
            FieldStyle.Text.Filled ->
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(resolvedLabel) },
                    placeholder =
                        if (hint.isNotEmpty()) {
                            { Text(hint) }
                        } else {
                            null
                        },
                    isError = showError,
                    supportingText = resolvedSupporting,
                    trailingIcon = c.trailingIcon,
                    leadingIcon = c.leadingIcon,
                    visualTransformation = c.visualTransformation,
                    keyboardOptions = keyboardOptions.copy(keyboardType = c.keyboardType),
                    keyboardActions = keyboardActions,
                    singleLine = c.singleLine,
                )
        }
    }
}

@Composable
public fun FormScope.IntField(
    state: FieldState<Int>,
    onValueChange: (Int) -> Unit,
    onFocusLost: () -> Unit,
    config: IntFieldConfig.() -> Unit = {},
) {
    val c = IntFieldConfig().apply(config)
    IntField(state, onValueChange, onFocusLost) {
        val resolvedSupporting: @Composable (() -> Unit)? =
            c.supportingText ?: if (showError) {
                { Text(errorMessage ?: "") }
            } else {
                null
            }
        when (c.style) {
            FieldStyle.Text.Outlined ->
                OutlinedTextField(
                    value = if (value == 0) "" else value.toString(),
                    onValueChange = {
                        onValueChange(it.filter { d -> d.isDigit() }.toIntOrNull() ?: 0)
                    },
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(label) },
                    placeholder =
                        if (hint.isNotEmpty()) {
                            { Text(hint) }
                        } else {
                            null
                        },
                    isError = showError,
                    supportingText = resolvedSupporting,
                    trailingIcon = c.trailingIcon,
                    leadingIcon = c.leadingIcon,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = c.singleLine,
                )
            FieldStyle.Text.Filled ->
                TextField(
                    value = if (value == 0) "" else value.toString(),
                    onValueChange = {
                        onValueChange(it.filter { d -> d.isDigit() }.toIntOrNull() ?: 0)
                    },
                    modifier = modifier.fillMaxWidth(),
                    label = { Text(label) },
                    placeholder =
                        if (hint.isNotEmpty()) {
                            { Text(hint) }
                        } else {
                            null
                        },
                    isError = showError,
                    supportingText = resolvedSupporting,
                    trailingIcon = c.trailingIcon,
                    leadingIcon = c.leadingIcon,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = c.singleLine,
                )
        }
    }
}

@Composable
public fun FormScope.BooleanField(
    state: FieldState<Boolean>,
    onCheckedChange: (Boolean) -> Unit,
    onFocusLost: () -> Unit,
    config: BooleanFieldConfig.() -> Unit = {},
) {
    val c = BooleanFieldConfig().apply(config)
    BooleanField(state, onCheckedChange, onFocusLost) {
        when (c.style) {
            FieldStyle.Toggle.Checkbox ->
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = modifier,
                )
            FieldStyle.Toggle.Switch ->
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = modifier,
                )
            FieldStyle.Toggle.CheckboxRow ->
                Column(modifier = modifier) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                        Text(label, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (showError) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            FieldStyle.Toggle.SwitchRow ->
                Column(modifier = modifier) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Switch(checked = checked, onCheckedChange = onCheckedChange)
                    }
                    if (showError) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
        }
    }
}

@Composable
public fun <T : Enum<T>> FormScope.EnumField(
    state: FieldState<T>,
    options: List<T>,
    onSelect: (T) -> Unit,
    config: EnumFieldConfig<T>.() -> Unit = {},
) {
    val c = EnumFieldConfig<T>().apply(config)
    EnumField(state, options, onSelect) {
        when (c.style) {
            FieldStyle.Picker.Dropdown -> {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = modifier,
                ) {
                    OutlinedTextField(
                        value = c.optionLabel(selected),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(label) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        isError = showError,
                        supportingText =
                            if (showError) {
                                { Text(errorMessage ?: "") }
                            } else {
                                null
                            },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(c.optionLabel(option)) },
                                onClick = {
                                    onSelect(option)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
            FieldStyle.Picker.RadioGroup ->
                Column(modifier = modifier) {
                    Text(label, style = MaterialTheme.typography.labelLarge)
                    options.forEach { option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selected == option,
                                onClick = { onSelect(option) },
                            )
                            Text(c.optionLabel(option), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (showError) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            FieldStyle.Picker.SegmentedButton ->
                Column(modifier = modifier) {
                    if (label.isNotEmpty()) {
                        Text(label, style = MaterialTheme.typography.labelLarge)
                    }
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        options.forEachIndexed { index, option ->
                            SegmentedButton(
                                selected = selected == option,
                                onClick = { onSelect(option) },
                                shape = SegmentedButtonDefaults.itemShape(index, options.size),
                                label = { Text(c.optionLabel(option)) },
                            )
                        }
                    }
                    if (showError) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
        }
    }
}
