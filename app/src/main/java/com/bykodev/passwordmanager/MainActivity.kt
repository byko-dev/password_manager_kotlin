package com.bykodev.passwordmanager

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.bykodev.passwordmanager.activity.PasswordsListActivity
import com.bykodev.passwordmanager.activity.RegisterActivity
import com.bykodev.passwordmanager.core.AppPreferencesManager
import com.bykodev.passwordmanager.database.models.User
import com.bykodev.passwordmanager.service.UserService
import com.bykodev.passwordmanager.ui.elements.Switcher
import com.bykodev.passwordmanager.ui.elements.YesNoSwitcher
import com.bykodev.passwordmanager.ui.theme.PasswordManagerTheme
import androidx.compose.material.icons.filled.ExitToApp as LoginIcon
import androidx.compose.material.icons.filled.Info as Visibility
import androidx.compose.material.icons.filled.Lock as VisibilityOff

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val appPreferencesManager = AppPreferencesManager(this)
            val isDarkThemeState = rememberSaveable { mutableStateOf(appPreferencesManager.getState(AppPreferencesManager.KEY_APP_MODE).toBoolean()) }

            PasswordManagerTheme(darkTheme = isDarkThemeState.value) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // A surface container using the 'background' color from the theme
                    LoginForm(isDarkThemeState)
                }
            }
        }
    }
}
@Composable
fun LoginForm(isDarkThemeState: MutableState<Boolean>) {
    val context = LocalContext.current
    val appStateManager = AppPreferencesManager(context)

    var username by remember { mutableStateOf(appStateManager.getState(AppPreferencesManager.KEY_USERNAME) ?: "") }
    var password by remember { mutableStateOf(appStateManager.getState(AppPreferencesManager.KEY_PASSWORD) ?: "") }
    var rememberMe by remember { mutableStateOf(appStateManager.getState(AppPreferencesManager.KEY_REMEMBER_ME).toBoolean()) }

    var passwordVisibility by remember { mutableStateOf(false) }

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
            text = stringResource(R.string.login_title),
            fontSize = 24.sp
         )

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
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, "")
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
                    val statusModel = UserService(context).loginAttempt(
                        User(
                            _username = username,
                            _password = password
                        )
                    )

                    if(rememberMe)
                    {
                        appStateManager.saveState(AppPreferencesManager.KEY_USERNAME, username)
                        appStateManager.saveState(AppPreferencesManager.KEY_PASSWORD, password)
                    }
                    else
                    {
                        appStateManager.removeState(AppPreferencesManager.KEY_USERNAME)
                        appStateManager.removeState(AppPreferencesManager.KEY_PASSWORD)
                    }

                    appStateManager.saveState(AppPreferencesManager.KEY_APP_MODE, isDarkThemeState.value.toString())
                    appStateManager.saveState(AppPreferencesManager.KEY_REMEMBER_ME, rememberMe.toString())

                    Toast.makeText(context, statusModel.message, Toast.LENGTH_SHORT).show()

                    if (statusModel.status)
                    {
                        val intent = Intent(context, PasswordsListActivity::class.java)
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.LoginIcon,
                    contentDescription = stringResource(R.string.icon_login_description),
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = stringResource(R.string.sing_in_button))
            }

            // Button for Register
            FilledTonalButton(
                onClick = {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = stringResource(R.string.icon_register_description),
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                )
                Text(text = stringResource(R.string.register_button))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = stringResource(R.string.remember_me_switcher))

            YesNoSwitcher(checked = rememberMe, modifier = Modifier, onCheckedChanged = {rememberMe = it} )
        }
        Spacer(modifier = Modifier.height(10.dp))

        ThemeSwitcher(isDarkThemeState.value, Modifier) {
            isDarkThemeState.value = it
        }
    }
}

@Composable
fun ThemeSwitcher(isDarkTheme: Boolean, modifier: Modifier, onCheckedChanged: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround) {
        Text(text = stringResource(R.string.theme_mod_switcher))

        Switcher(isDarkTheme, modifier, onCheckedChanged)
    }
}