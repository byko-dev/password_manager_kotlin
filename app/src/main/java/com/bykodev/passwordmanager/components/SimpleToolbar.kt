package com.bykodev.passwordmanager.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun SimpleToolbar(toolbarText: String = "Password Manager", content: @Composable (PaddingValues) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
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
                SimpleMainAppBar(toolbarText, onNavigationIconClicked = {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                })
            },
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMainAppBar(toolbarText: String, onNavigationIconClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(toolbarText) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClicked) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        }
    )
}
