package com.bykodev.passwordmanager.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.bykodev.passwordmanager.R
import com.bykodev.passwordmanager.components.GlobalBackHandler
import com.bykodev.passwordmanager.components.NavigationDrawer
import com.bykodev.passwordmanager.components.PasswordCardItem
import com.bykodev.passwordmanager.core.AppPreferencesManager
import com.bykodev.passwordmanager.service.PasswordService
import com.bykodev.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.coroutines.launch

class PasswordsListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appPreferencesManager = AppPreferencesManager(this)
            val darkTheme = appPreferencesManager.getState(AppPreferencesManager.KEY_APP_MODE).toBoolean()

            PasswordManagerTheme(darkTheme) {
                Surface {
                    PasswordsList()
                    GlobalBackHandler()
                }
            }
        }
    }
}

@Composable
fun PasswordsList() {
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(onNavigate = {}, context)
        }
    ) {
        Scaffold(
            topBar = {
                if (showSearchBar) {
                    SearchAppBar(searchText, onTextChange = { searchText = it }, onCloseClicked = { showSearchBar = false })
                } else {
                    MainAppBar(onSearchClicked = { showSearchBar = true }, onMenuClicked = { scope.launch { drawerState.open() } })
                }
            }
        ) { padding ->
            MyListScreen(Modifier.padding(padding), searchPhrase = searchText.text)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(onSearchClicked: () -> Unit, onMenuClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.icon_menu_description))
            }
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.icon_search_description))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(text: TextFieldValue, onTextChange: (TextFieldValue) -> Unit, onCloseClicked: () -> Unit) {
    SmallTopAppBar(
        title = {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                placeholder = { Text(text = stringResource(R.string.search_placeholder), fontSize = 18.sp) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClicked) {
                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.icon_back_description))
            }
        }
    )
}

@Composable
fun MyListScreen(modifier: Modifier = Modifier, searchPhrase : String = "") {
    val passwords = PasswordService().getUserPasswords()

    val filteredPasswords = if (searchPhrase.length >= 3) {
        passwords.filter { entry ->
            entry.title.contains(searchPhrase, ignoreCase = true) ||
                    entry.username.contains(searchPhrase, ignoreCase = true) ||
                    entry.type.contains(searchPhrase, ignoreCase = true) ||
                    entry.url.contains(searchPhrase, ignoreCase = true)
        }
    } else {
        passwords
    }

    LazyColumn(modifier = modifier) {
        items(filteredPasswords) { entry ->
            PasswordCardItem(
                entry,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}