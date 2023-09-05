package com.beardness.hica

import androidx.camera.core.ImageCapture
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun createFile(
        baseFolder: File,
        format: String,
        extension: String,
    ): File {
        val formatted = SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis())
        val child = formatted + extension
        return File(baseFolder, child)
    }

    fun getOutputFileOptions(
        photoFile: File
    ): ImageCapture.OutputFileOptions {
        val metadata = ImageCapture.Metadata()

        return ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .setMetadata(metadata)
            .build()
    }
}



