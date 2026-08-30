# Contributing to Formidable

Thank you for your interest in contributing to Formidable! This document provides guidelines and steps for contributing.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Setup](#development-setup)
- [Pull Request Process](#pull-request-process)
- [Coding Guidelines](#coding-guidelines)
- [Commit Messages](#commit-messages)

## Code of Conduct

This project follows the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to the maintainers.

## Getting Started

### Types of Contributions

We welcome the following contributions:

- **Bug reports** — Found a bug? Open an issue with a clear description and reproduction steps
- **Bug fixes** — Even better if you can fix it! Include a test case that fails without your fix
- **Documentation** — Improvements to README, API docs, or code comments
- **Feature requests** — Open an issue to discuss before implementing
- **New features** — Coordinate with maintainers first via an issue

### What We're NOT Looking For

- Changes that significantly increase the API surface without clear benefit
- Features that can be implemented as extensions rather than core changes
- Breaking changes without a strong justification

## How to Contribute

### Step 1: Find or Create an Issue

- Check [existing issues](https://github.com/WassimBeltaief/Formidable/issues) to avoid duplicates
- For bugs: Open an issue with reproduction steps
- For features: Open an issue to discuss the approach first

### Step 2: Fork and Clone

```bash
# Fork the repository on GitHub, then:
git clone https://github.com/YOUR_USERNAME/Formidable.git
cd Formidable
git remote add upstream https://github.com/WassimBeltaief/Formidable.git
```

### Step 3: Create a Branch

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/issue-description
```

### Step 4: Make Your Changes

- Follow the [coding guidelines](#coding-guidelines)
- Add tests for new functionality
- Update documentation if needed

### Step 5: Test Your Changes

```bash
# Run all tests
./gradlew test

# Build all modules
./gradlew build -x lint

# Build the sample app
./gradlew :formidable-sample-android:assembleDebug
```

### Step 6: Submit a Pull Request

- Push your branch to your fork
- Open a PR against the `master` branch
- Fill out the PR template
- Wait for review

## Development Setup

### Prerequisites

- JDK 17+
- Android Studio Hedgehog or later
- Android SDK 24+

### Project Structure

```
formidable-core        # Pure Kotlin — annotations, state, validators
formidable-ksp         # KSP processor — generates *Controller classes
formidable-compose     # Jetpack Compose integration
formidable-sample-android  # Demo app
```

### Building

```bash
# Build core modules
./gradlew :formidable-core:build :formidable-ksp:build :formidable-compose:build -x lint

# Run tests
./gradlew test

# Build sample app
./gradlew :formidable-sample-android:assembleDebug
```

### Running the Sample App

1. Open the project in Android Studio
2. Select the `formidable-sample-android` run configuration
3. Run on an emulator or device

## Pull Request Process

1. **Ensure tests pass** — Run `./gradlew test` before submitting
2. **One concern per PR** — Keep PRs focused on a single change
3. **Update documentation** — If your change affects the public API
4. **Add tests** — New features require tests; bug fixes should include a regression test
5. **Follow commit guidelines** — See [Commit Messages](#commit-messages)

### Review Process

- A maintainer will review your PR
- Address any feedback
- Once approved, a maintainer will merge your PR

## Coding Guidelines

### Kotlin Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful names for variables, functions, and classes
- Keep functions small and focused
- Prefer immutability (`val` over `var`)

### Architecture Rules

- `formidable-core` has **zero Android dependencies** — never add Android imports
- Extensions depend on `formidable-compose`, not vice versa
- Generated code goes in `formidable-ksp`

### Testing

- Write unit tests for new functionality
- Use descriptive test names that explain the scenario
- Test both success and failure cases

### Documentation

- Add KDoc to public APIs
- Keep comments minimal — code should be self-explanatory
- Update README if adding user-facing features

## Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>: <description>

[optional body]
```

### Types

- `feat:` — New feature
- `fix:` — Bug fix
- `docs:` — Documentation only
- `refactor:` — Code change that neither fixes a bug nor adds a feature
- `test:` — Adding or updating tests
- `chore:` — Build process, dependencies, etc.

### Examples

```
feat: Add @Pattern annotation for regex validation

fix: Prevent crash when form is submitted twice

docs: Update README installation instructions

refactor: Extract validation logic to separate function
```

---

## Questions?

If you have questions, feel free to:

- Open a [Discussion](https://github.com/WassimBeltaief/Formidable/discussions)
- Check existing issues for similar questions

Thank you for contributing!
