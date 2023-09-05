package com.beardness.hica

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this)
            .also { cameraProvider ->
                val listener = { continuation.resume(cameraProvider.get()) }
                val executor = ContextCompat.getMainExecutor(this)
                cameraProvider.addListener(listener, executor)
            }
    }

fun ImageCapture.takePicture(
    context: Context,
) {
    val outputDirectory = context.getOutputDirectory()

    val photoFile = Utils.createFile(
        baseFolder = outputDirectory,
        format = Constants.FILENAME,
        extension = Constants.PHOTO_EXTENSION,
    )

    val outputFileOptions = Utils.getOutputFileOptions(
        photoFile = photoFile,
    )

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri
                    ?: Uri.fromFile(photoFile)

                val mimeType = MimeTypeMap
                    .getSingleton()
                    .getMimeTypeFromExtension(savedUri.toFile().extension)

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(savedUri.toFile().absolutePath),
                    arrayOf(mimeType)
                ) { _, _ -> }
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}

fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs
        .firstOrNull()
        ?.let { file ->
            File(
                file,
                this.resources.getString(R.string.app_name),
            ).apply { mkdirs() }
        }

    mediaDir ?: return this.filesDir

    val isMediaDirExists = mediaDir.exists()

    return if (isMediaDirExists) mediaDir else this.filesDir

}