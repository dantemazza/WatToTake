package com.example.wat2take

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException


@Composable
fun UploadTranscript(navController: NavController) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen","PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen","PERMISSION DENIED")
        }
    }

    val context = LocalContext.current
    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { fileUri ->
        if (fileUri != null) {
            // Update the state with the Uri
            val filePath = fileUri.path ?: "null"
            Log.i("PATH: ", filePath )
            Log.i("URI", fileUri.toString())
            sendFile("/sdcard/Download/SSR_TSRPT.pdf")
            // onFilePicked(fileUri.toString(), context)
            navController.navigate("myCourses")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Upload your University of Waterloo Transcript Here",
                fontSize = 36.sp,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 32.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {
                pickFileLauncher.launch(arrayOf("application/pdf"))
            }) {
                Text(text = "Choose File", fontSize = 18.sp)
            }
            Button(onClick = {
                launcher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }) {
                Text(text = "Request Permissions", fontSize = 18.sp)
            }
        }
    }
}

fun sendFile(filePath: String) {
    val client = OkHttpClient()
    val file = File(filePath)
    val serverUrl = "https://7cac-192-159-178-206.ngrok.io//transcript"
    // val serverUrl = "https://wattotake.herokuapp.com/transcript"
    val request = Request.Builder()
        .url(serverUrl)
        // .post(RequestBody.create("text/html".toMediaType(), "hello"))
        .post(file.asRequestBody("application/pdf; charset=utf-8".toMediaType()))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                for ((name, value) in response.headers) {
                    println("$name: $value")
                }

                Log.i("RESPONSE", response.body!!.string())
            }
        }
    })
}

private fun sendRequestBody(url: String, requestBody: RequestBody) {

}

fun onFilePicked(filePath: String, context: Context) {
    MultipartUploadRequest(context, serverUrl = "https://025f-192-159-178-206.ngrok.io/transcript")
        .setMethod("POST")
        .addFileToUpload(
            filePath = filePath,
            parameterName = "file"
        ).startUpload()
}

