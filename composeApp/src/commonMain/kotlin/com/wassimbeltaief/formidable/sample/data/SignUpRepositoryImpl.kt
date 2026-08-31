package com.wassimbeltaief.formidable.sample.data

import com.wassimbeltaief.formidable.sample.domain.model.SignUpForm
import com.wassimbeltaief.formidable.sample.domain.repository.SignUpRepository

class SignUpRepositoryImpl : SignUpRepository {
    private var form = SignUpForm()

    override fun getForm(): SignUpForm = form

    override fun saveForm(form: SignUpForm) {
        this.form = form
    }
}
