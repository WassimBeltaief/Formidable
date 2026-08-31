package com.wassimbeltaief.formidable.sample.domain.usecase

import com.wassimbeltaief.formidable.sample.domain.model.SignUpForm
import com.wassimbeltaief.formidable.sample.domain.repository.SignUpRepository

class SaveSignUpFormUseCase(private val repository: SignUpRepository) {
    operator fun invoke(form: SignUpForm) = repository.saveForm(form)
}
