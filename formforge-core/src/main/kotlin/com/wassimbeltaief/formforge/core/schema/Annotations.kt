package com.wassimbeltaief.formforge.core.schema

import com.wassimbeltaief.formforge.core.validation.AsyncFieldValidator
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class FormSchema

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class Field(
    val label: String = "",
    val hint: String = "",
    val optional: Boolean = false,
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class NotBlank(
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class MinLength(
    val min: Int,
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class AsyncValidation(val validator: KClass<out AsyncFieldValidator<*>>)

