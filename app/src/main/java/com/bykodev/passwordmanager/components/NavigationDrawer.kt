package com.bykodev.passwordmanager.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bykodev.passwordmanager.MainActivity
import com.bykodev.passwordmanager.activity.AddPasswordActivity
import com.bykodev.passwordmanager.activity.PasswordsListActivity
import com.bykodev.passwordmanager.core.ApplicationContext

@Composable
fun NavigationDrawer(onNavigate: (String) -> Unit, context: Context) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(0.8f)
        .background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DrawerHeader()
            Spacer(modifier = Modifier.height(24.dp))
            DrawerItem(icon = Icons.Filled.Lock, label = "Passwords") {
                navigateToActivity(context, PasswordsListActivity::class.java)
            }
            DrawerItem(icon = Icons.Filled.Home, label = "Add New Password") {
                navigateToActivity(context, AddPasswordActivity::class.java)
            }
            DrawerItem(icon = Icons.Filled.ArrowBack, label = "Log out") {
                ApplicationContext.logOut()
                navigateToActivity(context, MainActivity::class.java)
            }
        }
    }

}

@Composable
fun DrawerHeader() {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "User Profile",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = ApplicationContext.getUsername(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label)
    }
}

fun navigateToActivity(context: Context,  activityClass: Class<*>)
{
    if (context !is Activity) {
        return
    }

    if (activityClass.isInstance(context)) {
        return
    }

    val intent = Intent(context, activityClass)
    context.startActivity(intent)
}



