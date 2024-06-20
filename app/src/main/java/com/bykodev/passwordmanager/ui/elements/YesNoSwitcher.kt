package com.bykodev.passwordmanager.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun YesNoSwitcher(checked: Boolean, modifier: Modifier, onCheckedChanged: (Boolean) -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChanged,
        modifier = modifier,
        thumbContent = {
            Icon(
                imageVector = if (checked) Icons.Filled.Check  else Icons.Filled.Close,
                contentDescription = null
            )
        }
    )
}

