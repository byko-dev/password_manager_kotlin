package com.bykodev.passwordmanager.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SimpleIconButton(
    text: String,
    icon: ImageVector,
    backgroundColor : Color = MaterialTheme.colorScheme.primary,
    onAddPasswordClick: () -> Unit) {
    Button(
        onClick = { onAddPasswordClick() },
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        elevation = ButtonDefaults.elevation(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White
        )
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}