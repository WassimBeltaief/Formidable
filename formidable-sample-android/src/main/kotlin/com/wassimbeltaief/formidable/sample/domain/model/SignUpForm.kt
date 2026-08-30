package com.wassimbeltaief.formidable.sample.domain.model

import com.wassimbeltaief.formidable.core.schema.AsyncValidation
import com.wassimbeltaief.formidable.core.schema.Email
import com.wassimbeltaief.formidable.core.schema.Field
import com.wassimbeltaief.formidable.core.schema.FormSchema
import com.wassimbeltaief.formidable.core.schema.IntRange
import com.wassimbeltaief.formidable.core.schema.MatchField
import com.wassimbeltaief.formidable.core.schema.MinLength
import com.wassimbeltaief.formidable.core.schema.MustBeTrue
import com.wassimbeltaief.formidable.core.schema.NotBlank
import com.wassimbeltaief.formidable.core.schema.Pattern
import com.wassimbeltaief.formidable.core.schema.RequiredIf
import com.wassimbeltaief.formidable.core.schema.VisibleWhen
import com.wassimbeltaief.formidable.sample.validation.UniqueUsernameValidator

@FormSchema
data class SignUpForm(
    @Field(label = "Username", hint = "Choose a username")
    @NotBlank(order = 1, message = "Username is required")
    @AsyncValidation(UniqueUsernameValidator::class)
    val username: String = "",

    @Field(label = "First Name", hint = "Your first name")
    @NotBlank(message = "First name is required")
    val firstName: String = "",

    @Field(label = "Last Name", hint = "Your last name")
    @NotBlank(message = "Last name is required")
    val lastName: String = "",

    @Field(label = "Password", hint = "At least 8 characters")
    @NotBlank(order = 1, message = "Password is required")
    @MinLength(order = 2, min = 8, message = "Password must be at least 8 characters")
    val password: String = "",

    @Field(label = "Confirm Password", hint = "Re-enter your password")
    @NotBlank(order = 1, message = "Please confirm your password")
    @MatchField(order = 2, targetField = "password", message = "Passwords do not match")
    val confirmPassword: String = "",

    @Field(label = "Nickname", hint = "How should we call you?", optional = true)
    val nickname: String? = null,

    @Field(label = "Age", hint = "Your age")
    @IntRange(min = 18, max = 120, message = "Must be between 18 and 120")
    val age: Int = 0,

    @Field(label = "Preferred Contact")
    val contactMethod: ContactMethod = ContactMethod.EMAIL,

    @Field(label = "Email", hint = "Your email address", optional = true)
    @VisibleWhen(targetField = "contactMethod", targetValue = "EMAIL")
    @RequiredIf(order = 1, targetField = "contactMethod", targetValue = "EMAIL", message = "Email is required")
    @Email(order = 2, message = "Please enter a valid email")
    val email: String? = null,

    @Field(label = "Phone", hint = "Your phone number", optional = true)
    @VisibleWhen(targetField = "contactMethod", targetValue = "PHONE")
    @RequiredIf(order = 1, targetField = "contactMethod", targetValue = "PHONE", message = "Phone is required")
    @Pattern(order = 2, regex = "^\\+?[0-9]{10,14}$", message = "Please enter a valid phone number")
    val phone: String? = null,

    @Field(label = "I accept the Terms & Conditions")
    @MustBeTrue(message = "You must accept the terms")
    val acceptTerms: Boolean = false,
)
