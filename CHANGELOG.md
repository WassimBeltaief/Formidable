# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.1.6] — 2026-09-01

### Added
- End-to-end compile-testing suite for `FormidableProcessor` using `dev.zacsweers.kctfork:ksp:0.9.0`
- 16 processor tests covering all field types (String, Boolean, Int, String?, Enum), all validators (@Email, @MinLength, @MaxLength, @NotBlank, @MustBeTrue, @MatchField, @RequiredIf), package placement, and the no-supported-fields error path

### Changed
- Kotlin 2.1.0 → 2.2.20, KSP 2.2.20-2.0.3, Compose Multiplatform 1.8.0 → 1.9.3, detekt 1.23.7 → 1.23.8, kotlinpoet 2.2.0 → 2.3.0

## [2.1.5] — 2026-08-01

### Added
- Initial release of Formidable form engine
- `formidable-core` module with annotations and state primitives
- `formidable-ksp` module for compile-time code generation
- `formidable-compose` module for Jetpack Compose integration

### Core Features
- `@FormSchema` annotation for defining form data classes
- `@Field` annotation with label, hint, and optional flag
- Generated `*Controller` classes with type-safe field access

### Validation
- Sync validators: `@NotBlank`, `@MinLength`, `@MaxLength`, `@Email`, `@Pattern`, `@IntRange`, `@MustBeTrue`
- Async validation: `@AsyncValidation` with `AsyncFieldValidator` interface
- Cross-field validation: `@MatchField`, `@RequiredIf`
- Conditional visibility: `@VisibleWhen`
- Validation ordering via `order` parameter

### Field Types
- `String` and `String?` fields
- `Boolean` fields
- `Int` fields
- `Enum` fields (auto-detected, no annotation needed)

### Compose Integration
- `Formidable {}` composable with `FormScope`
- `StringField`, `NullableStringField`, `IntField`, `BooleanField`, `EnumField`, `NullableEnumField`
- Automatic focus management and keyboard navigation
- `FieldScope` with pre-wired modifiers and keyboard options

[Unreleased]: https://github.com/WassimBeltaief/formidable/compare/main...HEAD
