package com.wassimbeltaief.formforge.ksp.codegen

internal fun String.capitalize(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
