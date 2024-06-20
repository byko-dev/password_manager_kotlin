package com.bykodev.passwordmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bykodev.passwordmanager.components.DropdownWithInput
import com.bykodev.passwordmanager.components.GlobalBackHandler
import com.bykodev.passwordmanager.components.SimpleIconButton
import com.bykodev.passwordmanager.components.SimpleToolbar
import com.bykodev.passwordmanager.core.AppPreferencesManager
import com.bykodev.passwordmanager.database.models.Password
import com.bykodev.passwordmanager.service.PasswordService
import com.bykodev.passwordmanager.ui.theme.PasswordManagerTheme
import androidx.compose.material.icons.filled.Info as Visibility
import androidx.compose.material.icons.filled.Lock as VisibilityOff

class AddPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val appPreferencesManager = AppPreferencesManager(this)
            val darkTheme = appPreferencesManager.getState(AppPreferencesManager.KEY_APP_MODE).toBoolean()

            PasswordManagerTheme(darkTheme) {
                Surface{
                    SimpleToolbar("Add New Password")
                    {
                        AddPasswordForm()
                        GlobalBackHandler()
                    }
                }
            }
        }
    }
}

@Composable
fun AddPasswordForm() {
    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var url by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    //set default value to 3 new lines to force display in rows in text field
    var description by remember { mutableStateOf("\n\n\n") }

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)

    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, "")
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownWithInput(
            options = listOf("Work", "School", "Social Media", "Banking", "Entertainment", "Email", "Home"),
            selectedOption = type,
            onOptionSelected = {type = it},
            label = "Type of password",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Additional description") },
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 3,
            singleLine = false
        )

        Spacer(modifier = Modifier.height(20.dp))


        SimpleIconButton(
            text = "Add",
            icon = Icons.Filled.Add
        ) {
            val passwordService = PasswordService()
            val statusModel = passwordService.createPassword(
                Password(
                    _title = title,
                    _username = username,
                    _password = password,
                    _url = url,
                    _type = type,
                    _description = description
                )
            )

            Toast.makeText(context, statusModel.message, Toast.LENGTH_SHORT).show()

            if (statusModel.status) {
                val intent = Intent(context, PasswordsListActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}


