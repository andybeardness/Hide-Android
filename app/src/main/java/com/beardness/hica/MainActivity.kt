package com.beardness.hica

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.beardness.hica.compose.ScreenView

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compose()
    }

    private fun compose() {
        setContent {
            HicaTheme {
                ScreenView(
                    imageCapture = viewModel.imageCapture,
                    lensFacing = viewModel.lensFacing,
                    onClickScreen = { viewModel.onTouchScreen(context = applicationContext) }
                )
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        viewModel.onVolumeKeyPressed(
            keyCode = keyCode,
            context = applicationContext,
        )

        return true
    }
}
