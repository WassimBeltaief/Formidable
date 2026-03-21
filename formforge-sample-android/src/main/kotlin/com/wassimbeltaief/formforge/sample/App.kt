package com.wassimbeltaief.formforge.sample

import android.app.Application
import com.wassimbeltaief.formforge.sample.data.SignUpRepositoryImpl
import com.wassimbeltaief.formforge.sample.domain.repository.SignUpRepository
import com.wassimbeltaief.formforge.sample.domain.usecase.GetSignUpFormUseCase
import com.wassimbeltaief.formforge.sample.domain.usecase.SaveSignUpFormUseCase
import com.wassimbeltaief.formforge.sample.ui.signup.SignUpViewModel

class App : Application() {

    private val repository: SignUpRepository = SignUpRepositoryImpl()

    val signUpViewModelFactory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(
                GetSignUpFormUseCase(repository),
                SaveSignUpFormUseCase(repository),
            ) as T
        }
    }
}
