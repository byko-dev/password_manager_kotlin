package com.bykodev.passwordmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bykodev.passwordmanager.MainActivity
import com.bykodev.passwordmanager.R
import com.bykodev.passwordmanager.core.AppPreferencesManager
import com.bykodev.passwordmanager.database.models.User
import com.bykodev.passwordmanager.service.UserService
import com.bykodev.passwordmanager.ui.theme.PasswordManagerTheme
import androidx.compose.material.icons.filled.ExitToApp as LoginIcon
import androidx.compose.material.icons.filled.Info as Visibility
import androidx.compose.material.icons.filled.Lock as VisibilityOff

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val appPreferencesManager = AppPreferencesManager(this)
            val darkTheme = appPreferencesManager.getState(AppPreferencesManager.KEY_APP_MODE).toBoolean()

            PasswordManagerTheme(darkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RegisterForm()
                }

            }
        }
    }
}

@Composable
fun RegisterForm() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }


    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.register_title),
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = stringResource(R.string.username_field)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(R.string.password_field)) },
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = retypePassword,
            onValueChange = { retypePassword = it },
            label = { Text(text = stringResource(R.string.retype_password_field)) },
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, contentDescription = stringResource(R.string.icon_retype_password_description))
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row {
            // Button for Login
            ElevatedButton(
                onClick = {

                    val statusModel = UserService(context).createAccount(
                        User(
                            _username = username,
                            _password = password
                        ),
                        retypePassword = retypePassword
                    )

                    Toast.makeText(context, statusModel.message, Toast.LENGTH_SHORT).show()

                    if (statusModel.status)
                    {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = stringResource(R.string.icon_sign_up_description),
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = stringResource(R.string.sign_up_button))
            }

            // Button for Register
            FilledTonalButton(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LoginIcon,
                    contentDescription = stringResource(R.string.icon_login_description),
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = stringResource(R.string.login_button))
            }
        }
    }
}