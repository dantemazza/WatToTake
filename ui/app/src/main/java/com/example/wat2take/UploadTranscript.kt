package com.example.wat2take

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
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
import com.example.wat2take.Global.Companion.SERVER_URL
import com.example.wat2take.viewmodels.AppDataStore
import com.example.wat2take.viewmodels.CourseRecsViewModel
import com.example.wat2take.viewmodels.CoursesViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
import java.net.ConnectException
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadTranscript(navController: NavController) {
    val context = LocalContext.current

    val appDataStore = AppDataStore(context)
    val coursesDataStore = CoursesViewModel(context)
    val courseRecsDataStore = CourseRecsViewModel(context)

    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { fileUri ->
        if (fileUri != null) {
            var path = ""
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
                try{
                    val copyPath = copyFileToInternalStorage(fileUri, "", context)
                    Log.i("copyPath", copyPath ?: "")
                    if(copyPath != null){
                        path = copyPath
                    }else{
                        Log.e("File Copy Error", "Could not copy file into local directory")
                    }
                }catch (e: Exception){
                    Log.e("File Copy Error", e.toString())
                }
            }else{
                val filePathUri = fileUri.path
                Log.i("filePathUri: ", filePathUri ?: "")

                if(filePathUri != null){
                    val filePath = filePathUri.replace("/document/raw:", "")
                    Log.i("filePath", filePath)
                    path = filePath
                }else{
                    Log.e("File Path URI Error", "Could not retreive file path from URI")
                }
            }
            sendFile(path, appDataStore, coursesDataStore, courseRecsDataStore)
            GlobalScope.launch {
                appDataStore.setAppLoading(true)
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
                    text = "Upload your University of Waterloo transcript here",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = {
                    pickFileLauncher.launch("application/pdf")
                }) {
                    Text(text = "Choose File", fontSize = 15.sp)
                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp),
                textAlign = TextAlign.Center,
                text ="Wat2Take requires device storage access in order to allow you to upload your " +
                    "UW Transcript. Please press the button below to grant us this permission"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { storagePermissionState.launchMultiplePermissionRequest() }) {
                Text("Request device storage permission")
            }
        }
    }
}

private fun copyFileToInternalStorage(uri: Uri, newDirName: String, context: Context): String? {
    val returnCursor: Cursor = context.getContentResolver().query(
        uri, arrayOf(
            OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
        ), null, null, null
    )!!

    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    val output: File
    if (newDirName != "") {
        val dir: File = File(context.getFilesDir().toString() + "/" + newDirName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        output = File(context.getFilesDir().toString() + "/" + newDirName + "/" + name)
    } else {
        output = File(context.getFilesDir().toString() + "/" + name)
    }
    try {
        val inputStream: InputStream = context.getContentResolver().openInputStream(uri)!!
        val outputStream = FileOutputStream(output)
        var read = 0
        val bufferSize = 1024
        val buffers = ByteArray(bufferSize)
        while (inputStream.read(buffers).also { read = it } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream.close()
        outputStream.close()
    } catch (e: Exception) {
        Log.e("File Copy Error", e.message!!)
    }
    return output.path
}

fun sendFile(
    filePath: String,
    appDataStore: AppDataStore,
    courseDataStore: CoursesViewModel,
    courseRecsDataStore: CourseRecsViewModel
) {
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();

    val file = File(filePath)
    val serverUrl = SERVER_URL
    val httpUrl = serverUrl.toHttpUrlOrNull()!!.newBuilder()
        .build()

    val formBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file",
            filePath,
            file.asRequestBody("application/pdf; charset=utf-8".toMediaType())
        )
        .build()

    val request = Request.Builder()
        .url(httpUrl)
        .post(formBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            GlobalScope.launch {
                appDataStore.setAppLoading(false)
                val cause = e.cause
                val errorString = if(cause is ConnectException){
                    "Cannot connect to the server"
                }else{
                    "Something went wrong with our server. " +
                            "Please ensure the PDF file you are sending is not encrypted"
                }
                appDataStore.setNetworkError(errorString)
            }
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) {
                    GlobalScope.launch {
                        appDataStore.setAppLoading(false)
                    }
                    throw IOException("Unexpected code $response")
                }

                for ((name, value) in response.headers) {
                    println("$name: $value")
                }

                try {
                    val responseBody = response.body!!.string()
                    Log.i("RESPONSE", responseBody)
                    val responseBodyJSON = JsonParser().parse(responseBody).asJsonObject
                    Log.i("Response Json", responseBodyJSON.toString())
                    val courseListJson = responseBodyJSON.getAsJsonArray("courses")
                    val courseRecsListJsonObject = responseBodyJSON.getAsJsonObject("recommendations")
                    val courseRecsListJsonArray = courseRecsListJsonObject.getAsJsonArray("recommendations")
                    Log.i("Response Course List JSON", courseListJson.toString())
                    Log.i("Response Course List Recs JSON", courseRecsListJsonArray.toString())
                    GlobalScope.launch {
                        courseDataStore.saveCourseList(courseListJson.toString())
                        courseRecsDataStore.saveCoursRecsList(courseRecsListJsonArray.toString())
                        appDataStore.setAppLoading(false)
                    }
                } catch (e: Exception) {
                    GlobalScope.launch {
                        appDataStore.setNetworkError("Error parsing JSON response from server")
                    }
                }
            }
        }
    })
}


