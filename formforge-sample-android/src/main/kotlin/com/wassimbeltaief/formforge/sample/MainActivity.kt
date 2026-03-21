package com.wassimbeltaief.formforge.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wassimbeltaief.formforge.sample.ui.signup.SignUpScreen
import com.wassimbeltaief.formforge.sample.ui.signup.SignUpViewModel
import com.wassimbeltaief.formforge.sample.ui.theme.FormForgeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SignUpViewModel by viewModels {
        (application as App).signUpViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormForgeTheme {
                SignUpScreen(viewModel)
            }
        }
    }
}
