package com.bykodev.passwordmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.bykodev.passwordmanager.R
import com.bykodev.passwordmanager.components.CopyOutlinedTextField
import com.bykodev.passwordmanager.components.DeleteAlertConfirmation
import com.bykodev.passwordmanager.components.DropdownWithInput
import com.bykodev.passwordmanager.components.GlobalBackHandler
import com.bykodev.passwordmanager.components.SimpleIconButton
import com.bykodev.passwordmanager.components.SimpleToolbar
import com.bykodev.passwordmanager.core.AppPreferencesManager
import com.bykodev.passwordmanager.database.models.Password
import com.bykodev.passwordmanager.helper.PasswordTypes
import com.bykodev.passwordmanager.service.PasswordService
import com.bykodev.passwordmanager.ui.theme.PasswordManagerTheme
import androidx.compose.material.icons.filled.Info as Visibility
import androidx.compose.material.icons.filled.Lock as VisibilityOff

class EditPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passwordId = intent.getIntExtra("passwordId", 0)

        val password = getPassword(passwordId)

        setContent {

            val appPreferencesManager = AppPreferencesManager(this)
            val darkTheme = appPreferencesManager.getState(AppPreferencesManager.KEY_APP_MODE).toBoolean()

            PasswordManagerTheme(darkTheme) {
                Surface{
                    SimpleToolbar("Edit Password")
                    {
                        EditPasswordForm(password)
                        GlobalBackHandler()
                    }
                }
            }
        }
    }
}

@Composable
fun EditPasswordForm(password: Password) {

    var title by remember { mutableStateOf(password.title) }
    var username by remember { mutableStateOf(password.username) }
    var passwordInput by remember { mutableStateOf(password.password) }
    var description by remember { mutableStateOf(password.description) }
    var url by remember { mutableStateOf(password.url) }
    var type by remember { mutableStateOf(password.type) }

    var passwordVisibility by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)

    ) {

        CopyOutlinedTextField(text = title,
            onValueChange = { title = it },
            label = stringResource(R.string.title_field)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CopyOutlinedTextField(text = username,
            onValueChange = { username = it },
            label = stringResource(R.string.username_field)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(stringResource(R.string.password_field)) },
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Row {
                    IconButton(onClick = {
                        Toast.makeText(context, getString(context, R.string.password_copied_message), Toast.LENGTH_SHORT).show()
                        clipboardManager.setText(AnnotatedString(passwordInput))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.icon_copy_password_description),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription =
                            if (passwordVisibility)
                                stringResource(R.string.icon_hide_password_description)
                            else
                                stringResource(R.string.icon_show_password_description),

                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CopyOutlinedTextField(text = url,
            onValueChange = { url = it },
            label = stringResource(R.string.url_field)
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownWithInput(
            options = PasswordTypes(context).getAllStrings(),
            selectedOption = type,
            onOptionSelected = {type = it},
            label = stringResource(R.string.type_of_password_field),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.additional_description_field)) },
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 3,
            singleLine = false
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            SimpleIconButton(
                text = stringResource(R.string.edit_button),
                icon = Icons.Filled.Add
            ) {
                val passwordService = PasswordService()

                password.title = title
                password.username = username
                password.password = passwordInput
                password.description = description
                password.url = url
                password.type = type

                val statusModel = passwordService.update(password)

                Toast.makeText(context, statusModel.message, Toast.LENGTH_SHORT).show()

                if (statusModel.status){
                    val intent = Intent(context, PasswordsListActivity::class.java)
                    context.startActivity(intent)
                }
            }

            SimpleIconButton(
                text = stringResource(R.string.delete_button),
                icon = Icons.Filled.Delete,
                onAddPasswordClick = {
                    showDialog = true
                },
                backgroundColor = MaterialTheme.colorScheme.error
            )

            if (showDialog)
            {
                DeleteAlertConfirmation(
                    onConfirm = {
                        val passwordService = PasswordService()
                        passwordService.delete(password.id)

                        Toast.makeText(context, getString(context, R.string.password_deleted_message), Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, PasswordsListActivity::class.java)
                        context.startActivity(intent)
                    },
                    onDismiss = {showDialog = false}
                )
            }

        }
    }
}

fun getPassword(passwordId: Int) : Password
{
    if (passwordId == 0)
        return Password()

    val passwordService = PasswordService()
    return passwordService.getPasswordById(passwordId)
}
