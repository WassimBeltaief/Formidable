package com.wassimbeltaief.formforge.sample.domain.usecase

import com.wassimbeltaief.formforge.sample.domain.model.SignUpForm
import com.wassimbeltaief.formforge.sample.domain.repository.SignUpRepository

class SaveSignUpFormUseCase(private val repository: SignUpRepository) {
    operator fun invoke(form: SignUpForm) = repository.saveForm(form)
}
