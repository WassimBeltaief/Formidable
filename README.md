# Formidable

<p align="center">
  <img src="docs/assets/screenshot.png" alt="Formidable — Headless forms for Jetpack Compose" width="100%" />
</p>

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![CI](https://github.com/WassimBeltaief/Formidable/actions/workflows/ci.yml/badge.svg)](https://github.com/WassimBeltaief/Formidable/actions/workflows/ci.yml)

Formidable generates type-safe form controllers from annotated kotlin data classes. Define your form once — get state management, validation, and Compose integration on Android, iOS, and Web.

## Highlights

- **KSP-powered** — No runtime reflection, all code generated at compile time
- **Type-safe** — Generated controllers with strongly-typed field access
- **Headless** — You own the UI, Formidable handles the state
- **Validation** — Sync, async, and cross-field validation out of the box
- **Multiplatform** — Android, iOS, and WASM via Compose Multiplatform

## Try it live

> 🌐 **[wassimbeltaief.github.io/Formidable](https://wassimbeltaief.github.io/Formidable/)** — interactive demo running in the browser

---

## Quick Start

Three steps. That's it.

### 1. Annotate a data class

```kotlin
@FormSchema
data class LoginForm(
    @Field(label = "Email")
    @Email
    val email: String = "",

    @Field(label = "Password")
    @MinLength(8)
    val password: String = "",
)
```

### 2. Use the generated controller

KSP generates `LoginFormController` — no boilerplate, no reflection.

```kotlin
class LoginViewModel : ViewModel() {
    val controller = LoginFormController()
}
```

### 3. Build your UI

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val emailState by viewModel.controller.email.collectAsState()
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
                supportingText = if (showError) { { Text(errorMessage ?: "") } } else null,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                modifier = modifier.fillMaxWidth(),
            )
        }

        Button(onClick = { /* submit */ }, enabled = isValid) {
            Text("Login")
        }
    }
}
```

You get validation, error display, focus management, and keyboard navigation for free.

---

## Installation

### Android (Jetpack Compose)

```kotlin
// build.gradle.kts
plugins {
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

dependencies {
    implementation("com.wassimbeltaief.formidable:formidable-core:2.0.1")
    implementation("com.wassimbeltaief.formidable:formidable-compose:2.0.1")
    ksp("com.wassimbeltaief.formidable:formidable-ksp:2.0.1")
}
```

### Kotlin Multiplatform (Compose Multiplatform)

KSP in KMP runs against `commonMain` metadata, which requires a small amount of extra wiring to make the generated sources visible to all targets.

```kotlin
// build.gradle.kts
plugins {
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.wassimbeltaief.formidable:formidable-core:2.0.1")
            implementation("com.wassimbeltaief.formidable:formidable-compose:2.0.1")
        }
    }
}

dependencies {
    // KSP processes @FormSchema in commonMain, generates a Controller for all targets
    add("kspCommonMainMetadata", "com.wassimbeltaief.formidable:formidable-ksp:2.0.1")
}

// Ensure KSP metadata runs before any compilation task
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (!name.startsWith("ksp")) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// Make generated sources visible in commonMain
kotlin.sourceSets.commonMain {
    kotlin.srcDir(layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin"))
}
```

---

## In-Depth Guide

### 1. Schema → Generated Controller

A `@FormSchema` data class is the single source of truth for your form. KSP reads it at compile time and generates a `*Controller` class — no runtime annotation scanning, no reflection.

**You write:**

```kotlin
@FormSchema
data class SignUpForm(
    @Field(label = "Username", hint = "Pick a username")
    @NotBlank
    val username: String = "",

    @Field(label = "Age")
    @IntRange(min = 18, max = 120)
    val age: Int = 0,

    @Field(label = "Accept terms")
    @MustBeTrue(message = "You must accept the terms")
    val acceptTerms: Boolean = false,
)
```

**KSP generates `SignUpFormController`:**

```kotlin
// Generated — do not edit
class SignUpFormController {
    val username: StateFlow<FieldState<String>>
    val age:      StateFlow<FieldState<Int>>
    val acceptTerms: StateFlow<FieldState<Boolean>>

    fun updateUsername(value: String)
    fun touchUsername()              // marks the field as interacted with

    fun updateAge(value: Int)
    fun touchAge()

    fun updateAcceptTerms(value: Boolean)
    fun touchAcceptTerms()

    val isValid: StateFlow<Boolean>  // true when all fields pass validation
    val data: SignUpForm             // snapshot of current values

    fun validateAllSync(): Boolean   // force-touch all fields and validate
    fun reset()                      // restore initial values, clear touched state
    fun clear()                      // reset to default (empty) values
    fun setFieldError(field: String, message: String) // inject server-side errors
}
```

Every property on the data class gets its own `StateFlow<FieldState<T>>` and a pair of `update*` / `touch*` functions. The controller is plain Kotlin — framework-agnostic, testable without Compose.

---

### 2. The Controller in Detail

#### FieldState

Each field's flow emits a `FieldState<T>`:

```kotlin
data class FieldState<T>(
    val value: T,           // current input value
    val errors: List<String>, // validation error messages (empty = valid)
    val isTouched: Boolean, // user has left the field at least once
    val isDirty: Boolean,   // value differs from the initial value
    val isValidating: Boolean, // async validation in progress
    val isVisible: Boolean, // controlled by @VisibleWhen
    val label: String,
    val hint: String,
)
```

`showError` is derived: errors are only surfaced once `isTouched = true`. This prevents error messages from flashing before the user has had a chance to type.

#### isValid

`isValid` is a `StateFlow<Boolean>` that recomputes whenever any field changes. It's `true` only when every visible, non-optional field passes all its validators.

#### validateAllSync / submit pattern

Call `validateAllSync()` on submit to force-touch all fields and return whether the form is valid:

```kotlin
Button(onClick = {
    if (viewModel.controller.validateAllSync()) {
        submitForm(viewModel.controller.data)
    }
})
```

#### Injecting server-side errors

After a failed API call, push errors back into specific fields:

```kotlin
viewModel.controller.setFieldError("username", "This username is already taken")
```

---

### 3. Connecting to Compose

Wrap your fields in `Formidable {}`. It wires up focus management, keyboard navigation (Next / Done), and scroll-to-error automatically.

```kotlin
@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    Formidable(state = viewModel.controller) {
        // Fields declared here are registered in focus order
        StringField(...) { /* your OutlinedTextField */ }
        IntField(...)    { /* your OutlinedTextField */ }
        BooleanField(...)  { /* your Checkbox row */ }

        Button(onClick = { viewModel.controller.validateAllSync() }) {
            Text("Sign up")
        }
    }
}
```

Each `*Field` lambda receives a `FieldScope` — a set of pre-computed properties you plug directly into your UI component.

---

### 4. FieldScope — What You Get Inside a Field

Every field lambda receives a typed `FieldScope`. You don't call any functions — just read the properties and pass them to your composable.

| Property | Type | Description |
|---|---|---|
| `value` | `T` | Current field value |
| `onValueChange` | `(T) -> Unit` | Call this when the user edits the field |
| `label` | `String` | From `@Field(label = ...)` |
| `hint` | `String` | From `@Field(hint = ...)` |
| `errors` | `List<String>` | All current validation error messages |
| `errorMessage` | `String?` | First error message, or `null` |
| `showError` | `Boolean` | `true` when field is touched **and** has errors |
| `isTouched` | `Boolean` | User has focused then left this field |
| `isDirty` | `Boolean` | Value differs from initial |
| `isValidating` | `Boolean` | Async validator is running |
| `modifier` | `Modifier` | Pre-wired with focus requester and scroll anchor |
| `keyboardOptions` | `KeyboardOptions` | Auto-configured (type + Next/Done) |
| `keyboardActions` | `KeyboardActions` | Moves focus to the next field, or submits |

The `modifier`, `keyboardOptions`, and `keyboardActions` are the most important: pass them directly to your text field and keyboard navigation just works.

---

### 5. Field Types

Formidable supports the following Kotlin types. Each maps to a typed Compose function and a corresponding `FieldScope<T>`.

| Kotlin type | Compose function | FieldScope value type |
|---|---|---|
| `String` | `StringField` | `String` |
| `String?` | `NullableStringField` | `String` |
| `Int` | `IntField` | `Int` |
| `Boolean` | `BooleanField` | `Boolean` |
| `Enum<T>` | `EnumField` | `T` (your enum) |
| `Enum<T>?` | `NullableEnumField` | `T?` |

---

### 6. Field Examples

#### String field

The most common case — a required text input with validation.

```kotlin
@FormSchema
data class LoginForm(
    @Field(label = "Email", hint = "you@example.com")
    @NotBlank
    @Email
    val email: String = "",
)
```

```kotlin
StringField(
    state = emailState,
    onValueChange = { controller.updateEmail(it) },
    onFocusLost = { controller.touchEmail() },
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(hint) },
        isError = showError,
        supportingText = if (showError) { { Text(errorMessage ?: "") } } else null,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth(),
    )
}
```

---

#### Boolean field (checkbox)

```kotlin
@Field(label = "I accept the terms and conditions")
@MustBeTrue(message = "You must accept the terms")
val acceptTerms: Boolean = false,
```

```kotlin
BooleanField(
    state = acceptTermsState,
    onCheckedChange = { controller.updateAcceptTerms(it) },
    onFocusLost = { controller.touchAcceptTerms() },
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = value, onCheckedChange = onValueChange)
            Text(label)
        }
        if (showError) {
            Text(errorMessage ?: "", color = MaterialTheme.colorScheme.error)
        }
    }
}
```

---

#### Int field

```kotlin
@Field(label = "Age")
@IntRange(min = 18, max = 120, message = "Must be between 18 and 120")
val age: Int = 0,
```

```kotlin
IntField(
    state = ageState,
    onValueChange = { controller.updateAge(it) },
    onFocusLost = { controller.touchAge() },
) {
    OutlinedTextField(
        value = if (value == 0) "" else value.toString(),
        onValueChange = { onValueChange(it.toIntOrNull() ?: 0) },
        label = { Text(label) },
        isError = showError,
        supportingText = if (showError) { { Text(errorMessage ?: "") } } else null,
        keyboardOptions = keyboardOptions, // already set to KeyboardType.Number
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth(),
    )
}
```

---

#### Optional string field

Use `String?` for fields that are truly optional. `NullableStringField` emits `null` when the user clears the input.

```kotlin
@Field(label = "Nickname", hint = "Optional")
val nickname: String? = null,
```

```kotlin
NullableStringField(
    state = nicknameState,
    onValueChange = { controller.updateNickname(it) },
    onFocusLost = { controller.touchNickname() },
) {
    OutlinedTextField(
        value = value,          // value is String (never null in scope)
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
    )
}
```

---

#### Enum field

Enum types are auto-detected — no annotation needed beyond `@Field`.

```kotlin
enum class ContactMethod { EMAIL, PHONE, SMS }

@Field(label = "Preferred contact")
val contactMethod: ContactMethod = ContactMethod.EMAIL,
```

```kotlin
EnumField(
    state = contactMethodState,
    options = ContactMethod.entries,
    onSelect = { controller.updateContactMethod(it) },
) {
    // selected: T  (the currently selected enum value)
    // options: List<T>  (all possible values)
    ExposedDropdownMenuBox(...) { /* your dropdown UI */ }
}
```

---

#### Async validation

Async validators run in a coroutine after a debounce delay. Use them for server-side checks like username availability.

```kotlin
@Field(label = "Username")
@AsyncValidation(UniqueUsernameValidator::class)
val username: String = "",
```

```kotlin
class UniqueUsernameValidator : AsyncFieldValidator<String> {
    override suspend fun validate(value: String): ValidationResult {
        delay(500) // debounce: runs 500ms after the user stops typing
        return if (api.isUsernameAvailable(value)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(listOf("Username already taken"))
        }
    }
}
```

While validating, `isValidating = true` is emitted on the field's state. Use it to show a loading indicator:

```kotlin
StringField(state = usernameState, ...) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        trailingIcon = if (isValidating) {
            { CircularProgressIndicator(modifier = Modifier.size(20.dp)) }
        } else null,
        isError = showError,
        supportingText = when {
            isValidating -> { { Text("Checking availability…") } }
            showError    -> { { Text(errorMessage ?: "") } }
            else -> null
        },
        modifier = modifier.fillMaxWidth(),
    )
}
```

---

#### Cross-field validation

##### Password confirmation with `@MatchField`

```kotlin
@FormSchema
data class SignUpForm(
    @Field(label = "Password")
    @MinLength(8)
    val password: String = "",

    @Field(label = "Confirm password")
    @MatchField("password", message = "Passwords do not match")
    val confirmPassword: String = "",
)
```

No extra code needed in the UI — the generated controller validates `confirmPassword` against `password` automatically.

##### Conditional required with `@RequiredIf`

```kotlin
@Field(label = "Email")
@RequiredIf(targetField = "contactMethod", targetValue = "EMAIL")
val email: String? = null,
```

`email` is required only when `contactMethod == "EMAIL"`. If the condition is false, the field is skipped during validation even if empty.

##### Conditional visibility with `@VisibleWhen`

`@VisibleWhen` hides or shows a field based on the current value of another field. The field's `FieldState.isVisible` updates reactively — when it becomes `false`, the field is also excluded from validation entirely.

```kotlin
enum class ContactMethod { EMAIL, PHONE, SMS }

@FormSchema
data class ContactForm(
    @Field(label = "Preferred contact")
    val contactMethod: ContactMethod = ContactMethod.EMAIL,

    @Field(label = "Phone number")
    @VisibleWhen(targetField = "contactMethod", targetValue = "PHONE")
    @RequiredIf(order = 1, targetField = "contactMethod", targetValue = "PHONE", message = "Phone is required")
    @Pattern(order = 2, regex = "^\\+?[0-9]{10,14}$", message = "Please enter a valid phone number")
    val phone: String? = null,
)
```

Three annotations work together here:

- **`@VisibleWhen`** — the field is shown only when `contactMethod == "PHONE"`. When hidden, it is skipped during validation regardless of its value.
- **`@RequiredIf(order = 1, ...)`** — when visible, the field must not be empty. The `order` parameter controls which validator runs first within this field.
- **`@Pattern(order = 2, ...)`** — once the presence check passes, the format is validated. Running it second means the user sees "Phone is required" before "invalid format" on an empty field.

In the UI, wrap the field in `AnimatedVisibility` using `isVisible` from the field state:

```kotlin
val phoneState by controller.phone.collectAsState()

AnimatedVisibility(visible = phoneState.isVisible) {
    NullableStringField(
        state = phoneState,
        onValueChange = { controller.updatePhone(it) },
        onFocusLost = { controller.touchPhone() },
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = showError,
            supportingText = if (showError) { { Text(errorMessage ?: "") } } else null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = modifier.fillMaxWidth(),
        )
    }
}
```

---

### 7. Validation Annotations Reference

#### String

| Annotation | Parameters | Description |
|---|---|---|
| `@NotBlank` | `message` | Must not be empty or whitespace |
| `@MinLength` | `min`, `message` | Minimum character count |
| `@MaxLength` | `max`, `message` | Maximum character count |
| `@Email` | `message` | Valid email address format |
| `@Pattern` | `regex`, `message` | Custom regular expression |

#### Number

| Annotation | Parameters | Description |
|---|---|---|
| `@IntRange` | `min`, `max`, `message` | Integer must be within `[min, max]` |

#### Boolean

| Annotation | Parameters | Description |
|---|---|---|
| `@MustBeTrue` | `message` | Must be `true` (e.g. terms acceptance) |

#### Cross-field

| Annotation | Parameters | Description |
|---|---|---|
| `@MatchField` | `targetField`, `message` | Must equal the value of another field |
| `@RequiredIf` | `targetField`, `targetValue`, `message` | Required when another field equals a value |
| `@VisibleWhen` | `field`, `predicate` | Visibility controlled by a predicate class |

#### Async

| Annotation | Parameters | Description |
|---|---|---|
| `@AsyncValidation` | `validator: KClass` | Runs a suspending validator on value change |

---

## Modules

| Module | Description |
|---|---|
| `formidable-core` | Annotations, `FieldState`, `ValidationResult`, validator interfaces |
| `formidable-ksp` | KSP processor — generates `*Controller` classes at compile time |
| `formidable-compose` | `Formidable {}`, `Field`, `ConditionalField`, `FieldScope` |

## Sample App

See [`composeApp`](composeApp/) for a complete sign-up form with async validation, password confirmation, conditional fields, and an enum dropdown — running on Android, iOS, and in the browser.

## Contributing

Contributions are welcome! Please read the [contributing guidelines](CONTRIBUTING.md) before submitting a pull request.

## License

[MIT](LICENSE) — Copyright © 2024-present Wassim Beltaief
