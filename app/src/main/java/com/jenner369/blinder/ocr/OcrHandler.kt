package com.jenner369.blinder.ocr

import android.graphics.Bitmap
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.googlecode.tesseract.android.TessBaseAPI
import com.jenner369.blinder.ui.home.ui.HomeViewModel
import java.io.File


class OcrHandler(private val dataPath: String) {

    private val tessBaseAPI = TessBaseAPI()

    private val googleRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    init {
        if (!tessBaseAPI.init(dataPath, "eng+spa+spa_old")) { // could be multiple languages, like "eng+deu+fra"
            // Error initializing Tesseract (wrong/inaccessible data path or not existing language file(s))
            // Release the native Tesseract instance
            throw RuntimeException("Error initializing Tesseract")
            tessBaseAPI.recycle();
        }
    }

    fun tesseractPerformOcr(bitmap: Bitmap): String {
        tessBaseAPI.setImage(bitmap)
        val text: String = tessBaseAPI.utF8Text
        return text
    }

    fun googlePerformOcr(bitmap: Bitmap): String {
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = googleRecognizer.process(image)
        Tasks.await(result)
        return result.result.text
    }

    companion object {
        var instance : HomeViewModel? = null
    }
}