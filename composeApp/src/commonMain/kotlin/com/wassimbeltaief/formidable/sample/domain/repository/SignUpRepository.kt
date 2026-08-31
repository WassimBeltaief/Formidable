package com.wassimbeltaief.formidable.sample.domain.repository

import com.wassimbeltaief.formidable.sample.domain.model.SignUpForm

interface SignUpRepository {
    fun getForm(): SignUpForm
    fun saveForm(form: SignUpForm)
}
