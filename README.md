# Calculator Course - Part 1

This archive contains **Step 1** of the course project:
- base calculator logic
- clean project structure
- adaptive Compose UI
- app icon placeholders
- splash screen theme
- error-safe input handling

## Implemented in this part
- Basic operations: `+`, `-`, `*`, `/`, `%`
- Decimal numbers
- Sign change
- Clear / delete last symbol
- Expression preview and result output
- Adaptive keypad layout for different screen widths
- Splash screen via Android 12+ theme API

## Architecture
- `domain` — calculator logic and models
- `ui` — screen state, ViewModel, Compose UI, components, theme
- `app` — entry point

## Notes
This project is prepared as the **first large stage** for your lab history.
Cloud sync, push notifications, widgets/platform API, pass key and biometrics are intentionally left for later parts.

## To run
1. Open in Android Studio.
2. Let Gradle sync dependencies.
3. Run the `app` module.

## Recommended stack
- Kotlin
- Jetpack Compose
- Material 3
- MVVM-style presentation + separated domain logic
