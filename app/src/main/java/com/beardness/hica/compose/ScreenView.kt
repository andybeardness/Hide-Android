package com.beardness.hica.compose

import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScreenView(
    imageCapture: ImageCapture,
    lensFacing: Int,
    onClickScreen: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val haptic = LocalHapticFeedback.current

    val cameraPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.CAMERA,
    )

    val externalStorageWritePermissionState = rememberPermissionState(
        permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }

        if (!externalStorageWritePermissionState.status.isGranted) {
            externalStorageWritePermissionState.launchPermissionRequest()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    onClickScreen()
                    haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                },
            )
    ) {
        CameraView(
            imageCapture = imageCapture,
            lensFacing = lensFacing,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        )
    }
}