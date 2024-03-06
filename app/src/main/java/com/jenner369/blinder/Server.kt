package com.jenner369.blinder

import android.os.Build
import androidx.annotation.RequiresApi
import com.jenner369.blinder.ocr.OcrHandler
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }

    val ocrHandler = OcrHandler(MainActivity.dataPath)


    routing {
        get("/api/data") {
            call.respond(mapOf("message" to "Hello, world!"))
        }
        post("/api/upload") {
            call.receiveMultipart().forEachPart { part ->
                if (part is PartData.FileItem) {
                    //Create a bitmap from the image
                    val bitmap = part.streamProvider().use { its -> android.graphics.BitmapFactory.decodeStream(its) }
                    try {
                        val result = ocrHandler.detectText(bitmap)
                        GlobalScope.launch {
                            OcrHandler.instance?.onRecognizedTextChanged(result, bitmap)
                        }
                        call.respond(HttpStatusCode.OK, mapOf("message" to "File uploaded successfully"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to e.toString()))
                    }
/*
                    try {
                        //Perform OCR
                        val result = ocrHandler.tesseractPerformOcr(bitmap)
                        GlobalScope.launch {
                            OcrHandler.instance?.onRecognizedTextChanged(result)
                        }
                        //Send result to View Model to display
                        call.respond(HttpStatusCode.OK, mapOf("message" to "File uploaded successfully"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to e.toString()))
                    }*/

                }
            }
        }
    }
}