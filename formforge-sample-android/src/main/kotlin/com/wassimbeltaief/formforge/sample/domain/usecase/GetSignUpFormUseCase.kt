package com.wassimbeltaief.formforge.sample.domain.usecase

import com.wassimbeltaief.formforge.sample.domain.model.SignUpForm
import com.wassimbeltaief.formforge.sample.domain.repository.SignUpRepository

class GetSignUpFormUseCase(private val repository: SignUpRepository) {
    operator fun invoke(): SignUpForm = repository.getForm()
}
