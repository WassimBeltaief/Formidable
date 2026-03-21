package com.wassimbeltaief.formforge.sample.ui.signup

import androidx.lifecycle.ViewModel
import com.wassimbeltaief.formforge.sample.domain.model.SignUpFormController
import com.wassimbeltaief.formforge.sample.domain.usecase.GetSignUpFormUseCase
import com.wassimbeltaief.formforge.sample.domain.usecase.SaveSignUpFormUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel(
    getSignUpForm: GetSignUpFormUseCase,
    private val saveSignUpForm: SaveSignUpFormUseCase,
) : ViewModel() {

    val controller = SignUpFormController(getSignUpForm())

    private val _submitted = MutableStateFlow(false)
    val submitted: StateFlow<Boolean> = _submitted

    fun submitForm() {
        if (controller.validateAllSync()) {
            saveSignUpForm(controller.data)
            _submitted.value = true
        }
    }
}
