package com.wassimbeltaief.formidable.sample

import android.app.Application
import com.wassimbeltaief.formidable.sample.data.SignUpRepositoryImpl
import com.wassimbeltaief.formidable.sample.domain.repository.SignUpRepository
import com.wassimbeltaief.formidable.sample.domain.usecase.GetSignUpFormUseCase
import com.wassimbeltaief.formidable.sample.domain.usecase.SaveSignUpFormUseCase
import com.wassimbeltaief.formidable.sample.ui.signup.SignUpViewModel

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
