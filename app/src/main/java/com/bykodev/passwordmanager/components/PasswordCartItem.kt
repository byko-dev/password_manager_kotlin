package com.bykodev.passwordmanager.components

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.bykodev.passwordmanager.R
import com.bykodev.passwordmanager.activity.EditPasswordActivity
import com.bykodev.passwordmanager.helper.PasswordTypes
import com.bykodev.passwordmanager.model.PasswordPreviewModel


@Composable
fun PasswordCardItem(
    passwordModel: PasswordPreviewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .clickable {
                    val intent = Intent(context, EditPasswordActivity::class.java)
                    intent.putExtra("passwordId", passwordModel.id)
                    context.startActivity(intent)
                }


        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                PasswordIcon(PasswordTypes(context).getIconByPasswordType(passwordModel.type))
                PasswordInformation(passwordModel.username, passwordModel.username)
                Spacer(Modifier.weight(1f))
                PasswordItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                )
            }
            if (expanded) {
                ExpandedCard(
                    passwordModel, modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}

@Composable
private fun PasswordItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun PasswordInformation(
    passwordTitle: String,
    passwordDescription: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = passwordTitle,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = passwordDescription,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun ExpandedCard(
    username: PasswordPreviewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.expanded_card_description),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = username.description.replace("\n", "").trimIndent(),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Text(
            text = stringResource(R.string.expanded_card_url),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = username.url,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
private fun PasswordIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(dimensionResource(R.dimen.icon_size_max))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .size(dimensionResource(R.dimen.icon_size_large))
        )
    }
}
