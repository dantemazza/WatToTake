package com.example.wat2take

import android.content.Context
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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import net.gotev.uploadservice.observer.request.RequestObserver
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest

@Composable
fun UploadTranscript(navController: NavController) {
    val context = LocalContext.current
    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { fileUri ->
        if (fileUri != null) {
            // Update the state with the Uri
            Log.i("URI", fileUri.toString())
            onFilePicked(fileUri.toString(), context)
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
        }
    }
}

fun onFilePicked(filePath: String, context: Context) {
    MultipartUploadRequest(context, serverUrl = "https://025f-192-159-178-206.ngrok.io/transcript")
        .setMethod("POST")
        .addFileToUpload(
            filePath = filePath,
            parameterName = "file"
        ).startUpload()
}

