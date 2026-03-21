package com.wassimbeltaief.formforge.sample.data

import com.wassimbeltaief.formforge.sample.domain.model.SignUpForm
import com.wassimbeltaief.formforge.sample.domain.repository.SignUpRepository

class SignUpRepositoryImpl : SignUpRepository {
    private var form = SignUpForm()
    override fun getForm(): SignUpForm = form
    override fun saveForm(form: SignUpForm) { this.form = form }
}
