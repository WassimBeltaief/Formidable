package com.wassimbeltaief.formforge.sample.domain.model

import com.wassimbeltaief.formforge.core.schema.Field
import com.wassimbeltaief.formforge.core.schema.FormSchema
import com.wassimbeltaief.formforge.core.schema.MustBeTrue
import com.wassimbeltaief.formforge.core.schema.NotBlank

@FormSchema
data class SignUpForm(
    @Field(label = "Username", hint = "Choose a username")
    @NotBlank(message = "Username is required")
    val username: String = "",

    @Field(label = "First Name", hint = "Your first name")
    @NotBlank(message = "First name is required")
    val firstName: String = "",

    @Field(label = "Last Name", hint = "Your last name")
    @NotBlank(message = "Last name is required")
    val lastName: String = "",

    @Field(label = "I accept the Terms & Conditions")
    @MustBeTrue(message = "You must accept the terms")
    val acceptTerms: Boolean = false,
)
