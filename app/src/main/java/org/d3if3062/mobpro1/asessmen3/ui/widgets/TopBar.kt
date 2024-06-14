package org.d3if3062.mobpro1.asessmen3.ui.widgets

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.d3if3062.mobpro1.asessmen3.R
import org.d3if3062.mobpro1.asessmen3.system.database.model.ApiProfile
import org.d3if3062.mobpro1.asessmen3.ui.component.Odading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWidget(
    title: String,
    apiProfile: List<ApiProfile>,
    appTheme: Boolean,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onAppThemeChange: (Boolean) -> Unit
) {
    val act = LocalContext.current as Activity
    CenterAlignedTopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            IconButton(onClick = { onAppThemeChange(!appTheme) }) {
                Icon(
                    painter = if (appTheme) painterResource(id = R.drawable.dark_mode) else painterResource(
                        id = R.drawable.light_mode
                    ),
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onShowDialogChange(!showDialog) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(apiProfile.firstOrNull()?.photoUrl)
                        .crossfade(true)
                        .build(),
                    error = {painterResource(id = R.drawable.profile_circle)},
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}