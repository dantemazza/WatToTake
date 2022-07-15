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
import androidx.navigation.NavController
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.*


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
            val filePathUri = fileUri.path ?: "null"
            Log.i("PATH: ", filePathUri )
            Log.i("URI", fileUri.toString())

            val storagePath = Environment.getExternalStorageDirectory().getPath()
            Log.i("Testing path", "Env path: $storagePath")

            val fileName = getFileData(fileUri, context)
            if (fileName != "") { // "" Is returned if file name not found from getFileData()
                Log.i("Update", "Received file name: $fileName")
                val filePath = "$storagePath/Download/$fileName"
                Log.i("Update", "File Path: $filePath")
                sendFile(filePath)
            } else {
                // Toast file not found
            }
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

fun getFileData(uri: Uri, context: Context) : String {
    val contentResolver = context.contentResolver

    // The query, because it only applies to a single document, returns only
    // one row. There's no need to filter, sort, or select fields,
    // because we want all fields for one document.
    val cursor: Cursor? = contentResolver.query(
        uri, null, null, null, null, null)

    cursor?.use {
        // moveToFirst() returns false if the cursor has 0 rows. Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (it.moveToFirst()) {

            val columnNames = it.columnNames
            Log.i("Upload", "Column Names: ${Arrays.toString(columnNames)}")
            // Note it's called "Display Name". This is
            // provider-specific, and might not necessarily be the file name.\
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                val displayName: String =
                    it.getString(displayNameIndex)
                Log.i("UPLOAD", "Display Name: $displayName")
                return displayName
            } else {
                Log.i("Upload", "No display Name")
            }
        }
    }
    return ""
}

fun sendFile(filePath: String) {
    val client = OkHttpClient()

    val file = File(filePath)
    val serverUrl = "https://7504-192-159-178-206.ngrok.io/transcript"
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

