package com.wassimbeltaief.formidable.core.schema

import com.wassimbeltaief.formidable.core.validation.AsyncFieldValidator
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
public annotation class MaxLength(
    val max: Int,
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class Email(
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class Pattern(
    val regex: String,
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class MustBeTrue(
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class IntRange(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val message: String = "",
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
public annotation class AsyncValidation(val validator: KClass<out AsyncFieldValidator<*>>)

