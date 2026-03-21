package com.wassimbeltaief.formforge.sample.domain.repository

import com.wassimbeltaief.formforge.sample.domain.model.SignUpForm

interface SignUpRepository {
    fun getForm(): SignUpForm
    fun saveForm(form: SignUpForm)
}
