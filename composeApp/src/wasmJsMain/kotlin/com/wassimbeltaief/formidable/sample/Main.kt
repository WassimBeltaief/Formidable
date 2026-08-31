package com.wassimbeltaief.formidable.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.wassimbeltaief.formidable.sample.data.SignUpRepositoryImpl
import com.wassimbeltaief.formidable.sample.domain.usecase.GetSignUpFormUseCase
import com.wassimbeltaief.formidable.sample.domain.usecase.SaveSignUpFormUseCase
import com.wassimbeltaief.formidable.sample.ui.signup.SignUpScreen
import com.wassimbeltaief.formidable.sample.ui.signup.SignUpViewModel
import com.wassimbeltaief.formidable.sample.ui.theme.FormidableTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        val repository = remember { SignUpRepositoryImpl() }
        val viewModel =
            remember {
                SignUpViewModel(
                    GetSignUpFormUseCase(repository),
                    SaveSignUpFormUseCase(repository),
                )
            }
        FormidableTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(modifier = Modifier.widthIn(max = 480.dp).fillMaxSize()) {
                    SignUpScreen(viewModel)
                }
            }
        }
    }
}
