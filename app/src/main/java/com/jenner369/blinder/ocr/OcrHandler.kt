package com.jenner369.blinder.ocr

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.google.android.gms.tasks.Tasks
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.google.api.services.vision.v1.model.Feature
import com.google.api.services.vision.v1.model.Image
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.googlecode.tesseract.android.TessBaseAPI
import com.jenner369.blinder.ui.home.ui.HomeViewModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Arrays


class OcrHandler(private val dataPath: String) {

    private val tessBaseAPI = TessBaseAPI()

    private val googleRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private var vision: Vision
    init {

        val visionBuilder = Vision.Builder(
            NetHttpTransport(),
            AndroidJsonFactory(),
            null
        )
        visionBuilder.setVisionRequestInitializer(
            VisionRequestInitializer("AIzaSyAY3NhLRriNmUT4XnFNJzx5Bfl-gfZCQlI")
        )
        this.vision = visionBuilder.build()

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

    @Throws(IOException::class)
    fun detectText(bitmap: Bitmap): String  {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        val inputImage = Image()
        inputImage.encodeContent(byteArray)

        val desiredFeature = Feature()
        desiredFeature.setType("TEXT_DETECTION")

        val request = AnnotateImageRequest()
        request.image = inputImage
        request.features = Arrays.asList(desiredFeature)

        val batchRequest = BatchAnnotateImagesRequest()

        batchRequest.requests = Arrays.asList(request)

        val batchResponse = vision.images().annotate(batchRequest).execute()

        val text = batchResponse.responses[0].fullTextAnnotation
        var textFound = "No se ha detectado texto"
        if (text != null) {
            textFound = text.text
        }
        return textFound
    }

    fun googlePerformOcr(bitmap: Bitmap): String {
        val bitmapFocused = applyFocusFilter(bitmap)
        val image = InputImage.fromBitmap(bitmapFocused, 0)
        val result = googleRecognizer.process(image)
        Tasks.await(result)
        return result.result.text
    }

    private fun applyFocusFilter(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val result = IntArray(pixels.size)
        val matrix = arrayOf(
            intArrayOf(0, -1, 0),
            intArrayOf(-1, 5, -1),
            intArrayOf(0, -1, 0)
        )
        val size = 3
        val offset = size / 2
        for (y in 0 until height) {
            for (x in 0 until width) {
                var r = 0
                var g = 0
                var b = 0
                for (i in 0 until size) {
                    for (j in 0 until size) {
                        val pixel = if (x + i - offset < 0 || y + j - offset < 0 ||
                            x + i - offset >= width || y + j - offset >= height
                        ) {
                            0
                        } else {
                            pixels[(y + j - offset) * width + (x + i - offset)]
                        }

                        val factor = matrix[i][j]
                        r += Color.red(pixel) * factor
                        g += Color.green(pixel) * factor
                        b += Color.blue(pixel) * factor
                    }
                }
                r = if (r > 255) 255 else if (r < 0) 0 else r
                g = if (g > 255) 255 else if (g < 0) 0 else g
                b = if (b > 255) 255 else if (b < 0) 0 else b
                result[y * width + x] = Color.rgb(r, g, b)
            }
        }

        return Bitmap.createBitmap(result, width, height, Bitmap.Config.ARGB_8888)
    }

    // Función para obtener el color promedio alrededor de un píxel
    private fun getAverageColorAroundPixel(bitmap: Bitmap, x: Int, y: Int, radius: Int): Int {
        var sumR = 0
        var sumG = 0
        var sumB = 0
        var count = 0

        for (i in -radius..radius) {
            for (j in -radius..radius) {
                val pixelX = x + i
                val pixelY = y + j
                if (pixelX in 0 until bitmap.width && pixelY in 0 until bitmap.height) {
                    val color = bitmap.getPixel(pixelX, pixelY)
                    sumR += Color.red(color)
                    sumG += Color.green(color)
                    sumB += Color.blue(color)
                    count++
                }
            }
        }

        return Color.rgb(sumR / count, sumG / count, sumB / count)
    }
    // Función para mejorar la imagen para reconocimiento de texto
    fun improveForTextRecognition(inputBitmap: Bitmap): Bitmap {
        val outputBitmap = inputBitmap.copy(inputBitmap.config, true)

        // Ajustar contraste y brillo
        val cm = ColorMatrix().apply {
            setScale(1.5f, 1.5f, 1.5f, 1f) // Ajustar contraste
            postConcat(ColorMatrix().apply { setSaturation(0f) }) // Convertir a escala de grises
        }

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(cm)
        }

        val canvas = android.graphics.Canvas(outputBitmap)
        canvas.drawBitmap(outputBitmap, 0f, 0f, paint)

        return applyFocusFilter(outputBitmap)
    }

    companion object {
        var instance : HomeViewModel? = null
    }
}