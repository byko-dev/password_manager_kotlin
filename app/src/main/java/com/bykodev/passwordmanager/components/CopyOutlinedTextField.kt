package com.bykodev.passwordmanager.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString

@Composable
fun CopyOutlinedTextField(text: String, onValueChange: (String) -> Unit, label: String) {

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "$label has been copied!", Toast.LENGTH_SHORT).show()
                clipboardManager.setText(AnnotatedString(text))
            }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Copy $label"
                )
            }
        }
    )
}



