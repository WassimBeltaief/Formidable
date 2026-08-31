# Formidable

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![CI](https://github.com/WassimBeltaief/formidable/actions/workflows/ci.yml/badge.svg)](https://github.com/WassimBeltaief/formidable/actions/workflows/ci.yml)

> Headless, schema-driven form engine for Compose Multiplatform

Formidable is a Kotlin Multiplatform library that generates type-safe form controllers from annotated data classes. Define your form schema once, get validation, state management, and Compose integration for free — on Android, iOS, and Web.

## Highlights

- **KSP-powered** — No runtime reflection, all code generated at compile time
- **Type-safe** — Generated controllers with strongly-typed field access
- **Headless** — You own the UI, Formidable handles the state
- **Validation** — Sync, async, and cross-field validation out of the box
- **Multiplatform** — Android, iOS, and WASM (Web) via Compose Multiplatform

## Try it live

> 🌐 **[wassimbeltaief.github.io/Formidable](https://wassimbeltaief.github.io/Formidable/)** — interactive demo running in the browser

## Requirements

- Kotlin 2.0+
- Compose Multiplatform 1.8+
- Android SDK 24+ (for Android target)

## Installation

Add the dependencies to your module's `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.wassimbeltaief.formidable:formidable-core:2.0.0")
            implementation("com.wassimbeltaief.formidable:formidable-compose:2.0.0")
        }
    }
}

dependencies {
    // KSP processes @FormSchema in commonMain, generates Controller for all targets
    add("kspCommonMainMetadata", "com.wassimbeltaief.formidable:formidable-ksp:2.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir(layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin"))
}
```

## Quick Start

### 1. Define your form schema

```kotlin
@FormSchema
data class LoginForm(
    @Field(label = "Email", hint = "Enter your email")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    val email: String = "",

    @Field(label = "Password", hint = "Enter your password")
    @NotBlank(message = "Password is required")
    @MinLength(min = 8, message = "Password must be at least 8 characters")
    val password: String = "",

    @Field(label = "Remember me")
    val rememberMe: Boolean = false,
)
```

### 2. Use the generated controller

KSP generates a `LoginFormController` class:

```kotlin
class LoginViewModel : ViewModel() {
    val controller = LoginFormController()
}
```

### 3. Build your UI with Compose

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val emailState by viewModel.controller.email.collectAsState()
    val passwordState by viewModel.controller.password.collectAsState()
    val isValid by viewModel.controller.isValid.collectAsState()

    Formidable {
        StringField(
            state = emailState,
            onValueChange = { viewModel.controller.updateEmail(it) },
            onFocusLost = { viewModel.controller.touchEmail() },
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                isError = showError,
                supportingText = if (showError) {{ Text(errorMessage ?: "") }} else null,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                modifier = modifier.fillMaxWidth(),
            )
        }

        // Password field, remember me checkbox...

        Button(
            onClick = { /* submit */ },
            enabled = isValid,
        ) {
            Text("Login")
        }
    }
}
```

## Validation Annotations

### String Validators

| Annotation | Description |
|------------|-------------|
| `@NotBlank` | Field must not be empty or blank |
| `@MinLength(min)` | Minimum character count |
| `@MaxLength(max)` | Maximum character count |
| `@Email` | Valid email format |
| `@Pattern(regex)` | Custom regex pattern |

### Number Validators

| Annotation | Description |
|------------|-------------|
| `@IntRange(min, max)` | Integer must be within range |

### Boolean Validators

| Annotation | Description |
|------------|-------------|
| `@MustBeTrue` | Checkbox must be checked (e.g., terms acceptance) |

### Cross-Field Validation

| Annotation | Description |
|------------|-------------|
| `@MatchField(targetField)` | Must match another field (e.g., password confirmation) |
| `@RequiredIf(targetField, targetValue)` | Required when another field has a specific value |
| `@VisibleWhen(targetField, targetValue)` | Show/hide based on another field's value |

### Async Validation

```kotlin
@Field(label = "Username")
@AsyncValidation(UniqueUsernameValidator::class)
val username: String = ""

class UniqueUsernameValidator : AsyncFieldValidator<String> {
    override suspend fun validate(value: String): ValidationResult {
        delay(300) // debounce
        val isAvailable = checkUsernameAvailability(value)
        return if (isAvailable) ValidationResult.Valid
               else ValidationResult.Invalid(listOf("Username already taken"))
    }
}
```

## Field Types

Formidable supports the following field types:

| Type | Compose Function | Notes |
|------|------------------|-------|
| `String` | `StringField` | Required string |
| `String?` | `NullableStringField` | Optional string |
| `Int` | `IntField` | Integer with keyboard type |
| `Boolean` | `BooleanField` | Checkbox/switch |
| `Enum<T>` | `EnumField` | Auto-detected, no annotation needed |
| `Enum<T>?` | `NullableEnumField` | Optional enum |

## Generated Controller API

For a `@FormSchema` class, KSP generates:

```kotlin
class LoginFormController {
    // Field state flows
    val email: StateFlow<FieldState<String>>
    val password: StateFlow<FieldState<String>>

    // Form-level state
    val isValid: StateFlow<Boolean>

    // Field operations
    fun updateEmail(value: String)
    fun touchEmail()

    // Form operations
    fun validateAllSync(): Boolean
    fun reset()
    fun clear()

    // Current form data
    val data: LoginForm
}
```

## Modules

| Module | Description |
|--------|-------------|
| `formidable-core` | Annotations, state primitives, validator interfaces |
| `formidable-ksp` | KSP processor that generates `*Controller` classes |
| `formidable-compose` | Jetpack Compose integration with `Formidable {}` and field scopes |

## Sample App

See [`formidable-sample-android`](formidable-sample-android/) for a complete sign-up form example with:
- Async username validation
- Password confirmation with `@MatchField`
- Conditional fields with `@VisibleWhen` and `@RequiredIf`
- Enum dropdown for contact method selection

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) before submitting a pull request.

## License

[MIT](LICENSE) - Copyright (c) 2024-present Wassim Beltaief
