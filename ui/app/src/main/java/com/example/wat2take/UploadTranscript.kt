package com.example.wat2take

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.gson.JsonParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadTranscript(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TranscriptDataStore(context)

    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { fileUri ->
        if (fileUri != null) {
            // Update the state with the Uri
            val filePathUri = fileUri.path
            Log.i("filePathUri: ", filePathUri ?: "")

            if(filePathUri != null){
                val filePath = filePathUri.replace("/document/raw:", "")
                Log.i("filePath", filePath)
                sendFile(filePath, dataStore)
            }

            GlobalScope.launch {
                dataStore.setLoadingKey(true)
            }
            navController.navigate("myCourses") // Navigate to courses and activate loading spinner
        }
    }

    val storagePermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    if (storagePermissionState.allPermissionsGranted) {
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
                    pickFileLauncher.launch("application/pdf")
                }) {
                    Text(text = "Choose File", fontSize = 18.sp)
                }
            }
        }
    } else {
        Column {
            Text("Wat2Take requires deivce storage access in order to allow you to upload your " +
                    "UW Transcript. Please press the button below to grant us this permission")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { storagePermissionState.launchMultiplePermissionRequest() }) {
                Text("Request device storage permission")
            }
        }
    }

//    val storagePermissionsGranted = dataStore.getStoragePermissionsGranted
//        .collectAsState(initial = false).value
//
//    val requestPermissionLauncher =
//        rememberLauncherForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                GlobalScope.launch {
//                    dataStore.setStoragePermissionGranted(true)
//                }
//            } else {
//                GlobalScope.launch {
//                    dataStore.setStoragePermissionGranted(false)
//                }
//            }
//        }
//
//    LaunchedEffect(Unit){
//        when {
//            ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                dataStore.setStoragePermissionGranted(true)
//            }
//            shouldShowRequestPermissionRationale() -> {
//                // In an educational UI, explain to the user why your app requires this
//                // permission for a specific feature to behave as expected. In this UI,
//                // include a "cancel" or "no thanks" button that allows the user to
//                // continue using your app without granting the permission.
//                showInContextUI(...)
//            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                    Manifest.permission.REQUESTED_PERMISSION)
//            }
//        }
//    }
}

fun sendFile(filePath: String, dataStore: TranscriptDataStore) {
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();

    val file = File(filePath)
    val serverUrl = "https://a69d-192-159-178-206.ngrok.io/transcript"
    //val serverUrl = "https://7504-192-159-178-206.ngrok.io/transcript"
//    val serverUrl = "https://ptsv2.com/t/2qrpx-1657916021/post"
//    val serverUrl = "https://wattotake.herokuapp.com/transcript"
    val httpUrl = serverUrl.toHttpUrlOrNull()!!.newBuilder()
        .build()

    val formBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", filePath, file.asRequestBody("application/pdf; charset=utf-8".toMediaType()))
        .build()

    val request = Request.Builder()
        .url(httpUrl)
        .post(formBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            GlobalScope.launch {
                dataStore.setLoadingKey(false)
            }
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                for ((name, value) in response.headers) {
                    println("$name: $value")
                }

                val responseBody = response.body!!.string()
                Log.i("RESPONSE", responseBody)
                val responseBodyJSON = JsonParser().parse(responseBody).asJsonObject
                Log.i("Response Json", responseBodyJSON.toString())
                val courseListJson = responseBodyJSON.getAsJsonArray("courses")
                Log.i("Response JSON", courseListJson.toString())
                GlobalScope.launch {
                    dataStore.saveCourseList(courseListJson.toString())
                    dataStore.setLoadingKey(false)
                }
            }
        }
    })
}


