InkPasswordScreenLibraryProject

[![](https://jitpack.io/v/inlacou/InkPasswordScreen.svg)](https://jitpack.io/#inlacou/InkPasswordScreen)

## Examples

Example for activity:
```kt
InkPasswordPasswordActivity.launch(/*activity*/ this, your_md5_string_variable, "name of your first pet", /*attempts*/ 3) {
  when(it) {
    InkPasswordAttemptStatus.DENIED -> TODO()
    InkPasswordAttemptStatus.DENIED_TOO_MANY_ATTEMPTS -> TODO()
    InkPasswordAttemptStatus.GRANTED -> TODO()
    InkPasswordAttemptStatus.ERROR -> TODO()
    InkPasswordAttemptStatus.CANCELLED -> TODO()
  }
}
```

Example for dialog:
```kt
InkPasswordPasswordDialog.showAsDialog(/*activity*/ this, your_md5_string_variable, "name of your grandmother", /*attempts*/ 3) {
  when(it) {
    InkPasswordAttemptStatus.DENIED -> TODO()
    InkPasswordAttemptStatus.DENIED_TOO_MANY_ATTEMPTS -> TODO()
    InkPasswordAttemptStatus.GRANTED -> TODO()
    InkPasswordAttemptStatus.ERROR -> TODO()
    InkPasswordAttemptStatus.CANCELLED -> TODO()
  }
}
```
