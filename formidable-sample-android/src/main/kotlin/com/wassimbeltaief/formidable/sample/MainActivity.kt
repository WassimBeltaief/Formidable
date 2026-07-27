package com.wassimbeltaief.formidable.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wassimbeltaief.formidable.sample.ui.signup.SignUpScreen
import com.wassimbeltaief.formidable.sample.ui.signup.SignUpViewModel
import com.wassimbeltaief.formidable.sample.ui.theme.FormidableTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SignUpViewModel by viewModels {
        (application as App).signUpViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormidableTheme {
                SignUpScreen(viewModel)
            }
        }
    }
}
