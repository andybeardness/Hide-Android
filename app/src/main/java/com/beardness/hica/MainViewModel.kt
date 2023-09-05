package com.beardness.hica

import android.content.Context
import android.view.KeyEvent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture

class MainViewModel {
    private val _imageCapture: ImageCapture = ImageCapture.Builder().build()
    private val _lensFacing = CameraSelector.LENS_FACING_BACK

    val imageCapture get() = _imageCapture
    val lensFacing get() = _lensFacing

    fun onVolumeKeyPressed(keyCode: Int, context: Context) {
        val isKeyCodeVolumeDown = keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
        val isKeyCodeVolumeUp = keyCode == KeyEvent.KEYCODE_VOLUME_UP

        val isKeyCodeVolumeButtons =
            isKeyCodeVolumeUp || isKeyCodeVolumeDown

        if (isKeyCodeVolumeButtons) {
            takePicture(context = context)
        }
    }

    fun onTouchScreen(context: Context) {
        takePicture(context = context)
    }

    private fun takePicture(context: Context) {
        imageCapture.takePicture(context)
    }
}